/*
 * Copyright (c) 2026, Luke Archer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.teleporthighlighter;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.JavaScriptCallback;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetPositionMode;
import net.runelite.api.widgets.WidgetSizeMode;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.worldmap.WorldMapPointManager;
import net.runelite.client.util.ImageUtil;

@Slf4j
@PluginDescriptor(
	name = "Map Teleport Highlighter",
	description = "Adds a button by the world map zoom controls that makes every teleport destination flash, jewellery included",
	tags = {"teleport", "world map", "map", "jewellery", "jewelry", "new player", "beginner", "flash", "key"}
)
public class TeleportHighlighterPlugin extends Plugin
{
	static final String CONFIG_GROUP = "teleporthighlighter";

	/** Space in pixels between the zoom out button and ours. */
	private static final int BUTTON_GAP = 2;
	/** Pixels of glow drawn round each icon. */
	private static final int GLOW_RADIUS = 3;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private TeleportHighlighterConfig config;

	@Inject
	private WorldMapPointManager worldMapPointManager;

	@Inject
	private SpriteManager spriteManager;

	@Inject
	private ConfigManager configManager;

	private final List<TeleportHighlightPoint> points = new ArrayList<>();
	/** Map icons by resource path, padded ready for the glow. */
	private final Map<String, BufferedImage> iconCache = new HashMap<>();

	/** Our button on the world map, or null while the map is closed. */
	private Widget button;
	private boolean highlighting;
	private boolean glowShowing;

	// Last zoom out button position, so we only move our button when it moves
	private int lastZoomX = Integer.MIN_VALUE;
	private int lastZoomY = Integer.MIN_VALUE;
	private int lastZoomW = -1;
	private int lastZoomH = -1;

	@Provides
	TeleportHighlighterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TeleportHighlighterConfig.class);
	}

	@Override
	protected void startUp()
	{
		spriteManager.addSpriteOverrides(TeleportHighlighterSprite.values());
		highlighting = false;
		buildPoints();
	}

	@Override
	protected void shutDown()
	{
		removePointsFromMap();
		points.clear();
		iconCache.clear();
		highlighting = false;
		spriteManager.removeSpriteOverrides(TeleportHighlighterSprite.values());
		clientThread.invoke(this::removeButton);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!CONFIG_GROUP.equals(event.getGroup()))
		{
			return;
		}

		clientThread.invoke(() ->
		{
			// Rebuild so a new glow colour or category change shows straight away
			removePointsFromMap();
			buildPoints();
			if (highlighting)
			{
				addPointsToMap();
			}
			lastZoomX = Integer.MIN_VALUE; // force the button to be repositioned
		});
	}

	@Subscribe
	public void onWidgetClosed(WidgetClosed event)
	{
		if (event.getGroupId() != InterfaceID.WORLDMAP || !event.isUnload())
		{
			return;
		}

		// The map's widgets, ours included, are gone now
		button = null;
		if (config.turnOffOnClose())
		{
			setHighlighting(false);
		}
	}

	@Subscribe
	public void onClientTick(ClientTick event)
	{
		updateButton();

		if (highlighting)
		{
			updateFlash();
		}
	}

	// ---------------------------------------------------------------------
	// Button
	// ---------------------------------------------------------------------

	private void updateButton()
	{
		final Widget zoomOut = client.getWidget(InterfaceID.Worldmap.ZOOM_OUT);
		if (zoomOut == null || zoomOut.isHidden())
		{
			button = null;
			return;
		}

		final Widget parent = zoomOut.getParent();
		if (parent == null)
		{
			return;
		}

		if (button == null || !isChildOf(button, parent))
		{
			createButton(parent);
		}

		// Follow the zoom out button if the map is resized
		if (zoomOut.getRelativeX() != lastZoomX || zoomOut.getRelativeY() != lastZoomY
			|| zoomOut.getWidth() != lastZoomW || zoomOut.getHeight() != lastZoomH)
		{
			positionButton(zoomOut);
		}
	}

	private void createButton(Widget parent)
	{
		button = parent.createChild(-1, WidgetType.GRAPHIC);
		button.setName("Teleports");
		button.setHasListener(true);
		button.setNoClickThrough(true);
		button.setOnOpListener((JavaScriptCallback) ev -> setHighlighting(!highlighting));
		updateButtonLook();

		lastZoomX = Integer.MIN_VALUE;
	}

	private void positionButton(Widget zoomOut)
	{
		final int size = zoomOut.getHeight();
		final int x;
		if (placeOnRight(zoomOut))
		{
			x = zoomOut.getRelativeX() + zoomOut.getWidth() + BUTTON_GAP;
		}
		else
		{
			x = zoomOut.getRelativeX() - size - BUTTON_GAP;
		}

		button.setXPositionMode(WidgetPositionMode.ABSOLUTE_LEFT);
		button.setYPositionMode(WidgetPositionMode.ABSOLUTE_TOP);
		button.setWidthMode(WidgetSizeMode.ABSOLUTE);
		button.setHeightMode(WidgetSizeMode.ABSOLUTE);
		button.setOriginalX(x);
		button.setOriginalY(zoomOut.getRelativeY());
		button.setOriginalWidth(size);
		button.setOriginalHeight(size);
		button.revalidate();

		lastZoomX = zoomOut.getRelativeX();
		lastZoomY = zoomOut.getRelativeY();
		lastZoomW = zoomOut.getWidth();
		lastZoomH = zoomOut.getHeight();
	}

	private boolean placeOnRight(Widget zoomOut)
	{
		switch (config.buttonSide())
		{
			case LEFT:
				return false;
			case RIGHT:
				return true;
			case AUTO:
			default:
				// Sit on the side away from zoom in so the two zoom buttons stay together
				final Widget zoomIn = client.getWidget(InterfaceID.Worldmap.ZOOM_IN);
				return zoomIn != null && zoomIn.getRelativeX() < zoomOut.getRelativeX();
		}
	}

	private void updateButtonLook()
	{
		if (button == null)
		{
			return;
		}

		button.setSpriteId(highlighting
			? TeleportHighlighterSprite.BUTTON_ON.getSpriteId()
			: TeleportHighlighterSprite.BUTTON_OFF.getSpriteId());
		button.setAction(0, highlighting ? "Hide teleports" : "Show teleports");
	}

	private void removeButton()
	{
		if (button == null)
		{
			return;
		}

		final Widget parent = button.getParent();
		if (parent != null)
		{
			final Widget[] children = parent.getDynamicChildren();
			for (int i = 0; i < children.length; i++)
			{
				if (children[i] == button)
				{
					// Hide rather than delete so the other dynamic children keep their indexes
					button.setHidden(true);
					button.setHasListener(false);
					button.setOnOpListener((Object[]) null);
					button.setAction(0, null);
					break;
				}
			}
		}
		button = null;
	}

	private static boolean isChildOf(Widget child, Widget parent)
	{
		final Widget[] children = parent.getDynamicChildren();
		if (children == null)
		{
			return false;
		}

		for (Widget w : children)
		{
			if (w == child)
			{
				return !child.isSelfHidden();
			}
		}
		return false;
	}

	// ---------------------------------------------------------------------
	// Highlighting
	// ---------------------------------------------------------------------

	private void setHighlighting(boolean on)
	{
		if (on == highlighting)
		{
			return;
		}

		highlighting = on;
		if (on)
		{
			addPointsToMap();
			glowShowing = true;
			updateFlash();
		}
		else
		{
			removePointsFromMap();
		}
		updateButtonLook();
	}

	private void updateFlash()
	{
		final boolean glow = !config.flash()
			|| (System.currentTimeMillis() / config.flashSpeed()) % 2 == 0;

		if (glow == glowShowing)
		{
			return;
		}

		glowShowing = glow;
		for (TeleportHighlightPoint point : points)
		{
			point.showGlow(glow);
		}
	}

	private void addPointsToMap()
	{
		for (TeleportHighlightPoint point : points)
		{
			if (isEnabled(point))
			{
				point.showGlow(glowShowing);
				worldMapPointManager.add(point);
			}
		}
	}

	private void removePointsFromMap()
	{
		// Remove by type, not equals(), so the core World Map plugin's own icons are never touched
		worldMapPointManager.removeIf(TeleportHighlightPoint.class::isInstance);
	}

	private boolean isEnabled(TeleportHighlightPoint point)
	{
		// The Wilderness switch overrides every group
		if (point.isWilderness() && !config.showWilderness())
		{
			return false;
		}

		// Jewellery, capes and diary rewards each have their own tick box
		if (point.getItemToggle() != null)
		{
			return isItemEnabled(point.getItemToggle());
		}
		return isTypeEnabled(point.getType());
	}

	/**
	 * Reads an item's tick box by its config key. The keys are declared in
	 * TeleportHighlighterConfig, which gives each one a default of true.
	 */
	private boolean isItemEnabled(ItemToggle toggle)
	{
		final Boolean on = configManager.getConfiguration(CONFIG_GROUP, toggle.getConfigKey(), Boolean.class);
		return on == null || on;
	}

	private boolean isTypeEnabled(TeleportType type)
	{
		switch (type)
		{
			case NORMAL_MAGIC:
				return config.showNormal();
			case ANCIENT_MAGICKS:
				return config.showAncient();
			case LUNAR_MAGIC:
				return config.showLunar();
			case ARCEUUS_MAGIC:
				return config.showArceuus();
			case JEWELLERY:
				// Every jewellery item has its own tick box, so this only catches new items without one
				return true;
			case SCROLL:
				return config.showScrolls();
			case MINIGAME:
				return config.showMinigames();
			case FAIRY_RING:
				return config.showFairyRings();
			case OTHER:
			default:
				return config.showOther();
		}
	}

	private void buildPoints()
	{
		points.clear();

		final Color colour = config.highlightColour();
		// Glow frames depend on the colour, so they're made fresh each rebuild
		final Map<String, BufferedImage> glowCache = new HashMap<>();

		for (TeleportLocationData data : TeleportLocationData.values())
		{
			final BufferedImage icon = loadIcon(data.getIconPath());
			final BufferedImage glow = glowCache.computeIfAbsent(data.getIconPath(),
				path -> makeGlowImage(icon, colour));

			points.add(new TeleportHighlightPoint(data.getType(), data.getLocation(),
				TeleportHighlightPoint.tooltipFor(data), icon, glow)
				.withItemToggle(ItemToggle.fromItem(data.getItem())));
		}

		final BufferedImage minigameIcon = loadIcon(MinigameTeleport.ICON_PATH);
		final BufferedImage minigameGlow = makeGlowImage(minigameIcon, colour);
		for (MinigameTeleport minigame : MinigameTeleport.values())
		{
			points.add(new TeleportHighlightPoint(TeleportType.MINIGAME, minigame.getLocation(),
				TeleportHighlightPoint.tooltipFor(minigame), minigameIcon, minigameGlow));
		}

		final BufferedImage fairyIcon = loadIcon(FairyRing.ICON_PATH);
		final BufferedImage fairyGlow = makeGlowImage(fairyIcon, colour);
		for (FairyRing ring : FairyRing.values())
		{
			points.add(new TeleportHighlightPoint(TeleportType.FAIRY_RING, ring.getLocation(),
				TeleportHighlightPoint.tooltipFor(ring), fairyIcon, fairyGlow));
		}
	}

	// ---------------------------------------------------------------------
	// Images
	// ---------------------------------------------------------------------

	/**
	 * Loads a map icon once and keeps it, so changing a setting doesn't
	 * read every image again.
	 */
	private BufferedImage loadIcon(String path)
	{
		return iconCache.computeIfAbsent(path,
			p -> padImage(ImageUtil.loadImageResource(TeleportHighlighterPlugin.class, p), GLOW_RADIUS));
	}

	/**
	 * Adds a clear border so the normal and glowing images are the same size and
	 * the icon doesn't jump about when it flashes.
	 */
	private static BufferedImage padImage(BufferedImage image, int pad)
	{
		final BufferedImage out = new BufferedImage(image.getWidth() + pad * 2, image.getHeight() + pad * 2, BufferedImage.TYPE_INT_ARGB);
		out.getGraphics().drawImage(image, pad, pad, null);
		return out;
	}

	/**
	 * Makes the "lit up" frame: a soft coloured halo round the icon's shape,
	 * with the icon itself brightened on top.
	 */
	private static BufferedImage makeGlowImage(BufferedImage padded, Color colour)
	{
		final int w = padded.getWidth();
		final int h = padded.getHeight();
		final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		final int rgb = colour.getRGB() & 0xFFFFFF;

		for (int y = 0; y < h; y++)
		{
			for (int x = 0; x < w; x++)
			{
				final int src = padded.getRGB(x, y);
				final int srcAlpha = src >>> 24;

				// Halo strength from the distance to the nearest solid pixel
				double nearest = Double.MAX_VALUE;
				for (int dy = -GLOW_RADIUS; dy <= GLOW_RADIUS; dy++)
				{
					for (int dx = -GLOW_RADIUS; dx <= GLOW_RADIUS; dx++)
					{
						final int nx = x + dx;
						final int ny = y + dy;
						if (nx < 0 || ny < 0 || nx >= w || ny >= h || (padded.getRGB(nx, ny) >>> 24) < 64)
						{
							continue;
						}
						nearest = Math.min(nearest, Math.sqrt(dx * dx + dy * dy));
					}
				}

				int haloAlpha = 0;
				if (nearest <= GLOW_RADIUS)
				{
					haloAlpha = (int) (255 * (1.0 - nearest / (GLOW_RADIUS + 1.0)));
				}

				if (srcAlpha == 0)
				{
					out.setRGB(x, y, (haloAlpha << 24) | rgb);
					continue;
				}

				// Brighten the icon 25% towards white, then lay it over the halo
				final int r = brighten((src >> 16) & 0xFF);
				final int g = brighten((src >> 8) & 0xFF);
				final int b = brighten(src & 0xFF);
				final int icon = (r << 16) | (g << 8) | b;

				out.setRGB(x, y, blend(icon, srcAlpha, rgb, haloAlpha));
			}
		}
		return out;
	}

	private static int brighten(int channel)
	{
		return channel + (int) ((255 - channel) * 0.25);
	}

	/** Draws a colour with alpha {@code topA} over another with alpha {@code bottomA}. */
	private static int blend(int top, int topA, int bottom, int bottomA)
	{
		final double ta = topA / 255.0;
		final double ba = bottomA / 255.0;
		final double outA = ta + ba * (1 - ta);
		if (outA <= 0)
		{
			return 0;
		}

		int result = 0;
		for (int shift = 0; shift <= 16; shift += 8)
		{
			final double t = (top >> shift) & 0xFF;
			final double b = (bottom >> shift) & 0xFF;
			final int c = (int) Math.round((t * ta + b * ba * (1 - ta)) / outA);
			result |= (c & 0xFF) << shift;
		}
		return ((int) Math.round(outA * 255) << 24) | result;
	}
}
