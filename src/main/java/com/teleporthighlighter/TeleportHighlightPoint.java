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

import java.awt.image.BufferedImage;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;

/**
 * A teleport icon on the world map that can swap between its normal look
 * and a glowing look to make it flash.
 */
class TeleportHighlightPoint extends WorldMapPoint
{
	private static final String WILDERNESS_WARNING = "<br>WILDERNESS - other players can attack you";

	private final TeleportType type;
	private final boolean wilderness;
	private final BufferedImage normalImage;
	private final BufferedImage glowImage;

	/** Set on items with their own tick box (jewellery, capes, diary rewards). */
	private ItemToggle itemToggle;

	TeleportHighlightPoint(TeleportType type, WorldPoint location, String tooltip, BufferedImage normalImage, BufferedImage glowImage)
	{
		super(location, glowImage);
		this.type = type;
		this.wilderness = Wilderness.contains(location);
		this.normalImage = normalImage;
		this.glowImage = glowImage;
		setTooltip(wilderness ? tooltip + WILDERNESS_WARNING : tooltip);
	}

	TeleportType getType()
	{
		return type;
	}

	boolean isWilderness()
	{
		return wilderness;
	}

	ItemToggle getItemToggle()
	{
		return itemToggle;
	}

	TeleportHighlightPoint withItemToggle(ItemToggle itemToggle)
	{
		this.itemToggle = itemToggle;
		return this;
	}

	void showGlow(boolean glow)
	{
		setImage(glow ? glowImage : normalImage);
	}

	/**
	 * Two line tooltip written for new players: what it is, then how to use it.
	 * Lines are split on &lt;br&gt; by the world map overlay.
	 */
	static String tooltipFor(TeleportLocationData data)
	{
		final ItemToggle toggle = ItemToggle.fromItem(data.getItem());
		final String category = toggle != null ? toggle.getGroup().getLabel() : data.getType().getLabel();

		// Spells: "Varrock Teleport" / "Standard spellbook - Magic level 25"
		if (data.getItem() == null)
		{
			return data.getDestination() + " Teleport<br>" + category + " - Magic level " + data.getMagicLevel();
		}

		// Items with several destinations: "Barbarian Assault" / "Jewellery - Games Necklace"
		if (data.getDestination() != null)
		{
			// Obelisks and the canoe list a bare Wilderness level, e.g. "19"
			final String destination = data.getDestination().matches("\\d+")
				? "Wilderness level " + data.getDestination()
				: data.getDestination();
			return destination + "<br>" + category + " - " + data.getItem();
		}

		// Items with one destination: "Digsite Teleport" / "Scroll / tablet"
		return data.getItem() + "<br>" + category;
	}

	/**
	 * "Fishing Trawler" / "Minigame teleport (Grouping tab)" / "Needs: Fishing level 15"
	 */
	static String tooltipFor(MinigameTeleport minigame)
	{
		String tooltip = minigame.getName() + "<br>" + TeleportType.MINIGAME.getLabel() + " (Grouping tab)";
		if (minigame.getRequirement() != null)
		{
			tooltip += "<br>Needs: " + minigame.getRequirement();
		}
		return tooltip;
	}

	/**
	 * "Fairy ring - AIQ" / "Needs a dramen or lunar staff"
	 */
	static String tooltipFor(FairyRing ring)
	{
		return TeleportType.FAIRY_RING.getLabel() + " - " + ring.getCode() + "<br>Needs a dramen or lunar staff";
	}
}
