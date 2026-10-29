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

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(TeleportHighlighterPlugin.CONFIG_GROUP)
public interface TeleportHighlighterConfig extends Config
{
	enum ButtonSide
	{
		AUTO,
		LEFT,
		RIGHT
	}

	@ConfigItem(
		keyName = "showWilderness",
		name = "Show Wilderness teleports",
		description = "Off: teleports that land in the Wilderness are hidden, whichever group they are in. On: they show, with a warning in the tooltip. Ferox Enclave counts as safe",
		position = 0
	)
	default boolean showWilderness()
	{
		return false;
	}

	@ConfigSection(
		name = "Spellbooks",
		description = "Teleport spells from each spellbook. Click to open",
		position = 1,
		closedByDefault = true
	)
	String spellbooksSection = "spellbooks";

	@ConfigSection(
		name = "Jewellery",
		description = "Pick which jewellery teleports flash. Click to open",
		position = 2,
		closedByDefault = true
	)
	String jewellerySection = "jewellery";

	@ConfigSection(
		name = "Capes",
		description = "Pick which cape teleports flash. Click to open",
		position = 3,
		closedByDefault = true
	)
	String capesSection = "capes";

	@ConfigSection(
		name = "Achievement diary rewards",
		description = "Pick which diary reward teleports flash. Click to open",
		position = 4,
		closedByDefault = true
	)
	String diarySection = "diary";

	@ConfigSection(
		name = "Travel",
		description = "Fairy rings, minigame teleports, scrolls and tablets. Click to open",
		position = 5,
		closedByDefault = true
	)
	String travelSection = "travel";

	@ConfigSection(
		name = "Quest and reward items",
		description = "Other teleport items. Click to open",
		position = 6,
		closedByDefault = true
	)
	String questItemsSection = "questItems";

	@ConfigSection(
		name = "Appearance",
		description = "How the flashing and the button look",
		position = 7
	)
	String appearanceSection = "appearance";

	@ConfigItem(
		keyName = "showNormal",
		name = "Standard spellbook",
		description = "Varrock, Lumbridge, Falador, Camelot, Ardougne and the other standard spells",
		position = 0,
		section = spellbooksSection
	)
	default boolean showNormal()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showAncient",
		name = "Ancient Magicks",
		description = "Ancient spellbook teleports",
		position = 1,
		section = spellbooksSection
	)
	default boolean showAncient()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showLunar",
		name = "Lunar spellbook",
		description = "Lunar spellbook teleports",
		position = 2,
		section = spellbooksSection
	)
	default boolean showLunar()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showArceuus",
		name = "Arceuus spellbook",
		description = "Arceuus spellbook teleports",
		position = 3,
		section = spellbooksSection
	)
	default boolean showArceuus()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showAmuletOfGlory",
		name = "Amulet of glory",
		description = "Edgeville, Karamja, Draynor Village, Al-Kharid",
		position = 0,
		section = jewellerySection
	)
	default boolean showAmuletOfGlory()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRingOfDueling",
		name = "Ring of dueling",
		description = "Emir's Arena, Ferox Enclave, Castle Wars, Fortis Colosseum",
		position = 1,
		section = jewellerySection
	)
	default boolean showRingOfDueling()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showGamesNecklace",
		name = "Games necklace",
		description = "Barbarian Assault, Burthorpe Games Room, Tears of Guthix, Corporeal Beast, Wintertodt Camp",
		position = 2,
		section = jewellerySection
	)
	default boolean showGamesNecklace()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSkillsNecklace",
		name = "Skills necklace",
		description = "Fishing Guild, Mining Guild, Crafting Guild, Cooking Guild, Woodcutting Guild, Farming Guild",
		position = 3,
		section = jewellerySection
	)
	default boolean showSkillsNecklace()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showCombatBracelet",
		name = "Combat bracelet",
		description = "Warriors' Guild, Champions' Guild, Edgeville Monastery, Ranging Guild",
		position = 4,
		section = jewellerySection
	)
	default boolean showCombatBracelet()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRingOfWealth",
		name = "Ring of wealth",
		description = "Miscellania, Grand Exchange, Falador Park, Dondakan",
		position = 5,
		section = jewellerySection
	)
	default boolean showRingOfWealth()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSlayerRing",
		name = "Slayer ring",
		description = "Slayer masters and Slayer dungeons (8 locations)",
		position = 6,
		section = jewellerySection
	)
	default boolean showSlayerRing()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showDigsitePendant",
		name = "Digsite pendant",
		description = "Digsite, House on the Hill, Lithkren",
		position = 7,
		section = jewellerySection
	)
	default boolean showDigsitePendant()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showBurningAmulet",
		name = "Burning amulet",
		description = "Chaos Temple (lvl 15), Bandit Camp (lvl 17), Lava Maze (lvl 41)",
		position = 8,
		section = jewellerySection
	)
	default boolean showBurningAmulet()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showNecklaceOfPassage",
		name = "Necklace of passage",
		description = "Wizards' Tower, Jorral's Outpost, Desert eagle station of the eagle transport system, Wyrmscraig",
		position = 9,
		section = jewellerySection
	)
	default boolean showNecklaceOfPassage()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSailorsAmulet",
		name = "Sailors' amulet",
		description = "The Pandemonium, Port Roberts, Red Rock, Deepfin Point",
		position = 10,
		section = jewellerySection
	)
	default boolean showSailorsAmulet()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showGiantsoulAmulet",
		name = "Giantsoul amulet",
		description = "Bryophyta's Lair, Obor's Lair, Branda and Eldric's Lair",
		position = 11,
		section = jewellerySection
	)
	default boolean showGiantsoulAmulet()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showPendantOfAtes",
		name = "Pendant of ates",
		description = "North Aldarin, The Darkfrost, Ralos' Rise, Twilight Temple, Kastori, Nemus Retreat",
		position = 12,
		section = jewellerySection
	)
	default boolean showPendantOfAtes()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showCowbellAmulet",
		name = "Cowbell amulet",
		description = "Cow field",
		position = 13,
		section = jewellerySection
	)
	default boolean showCowbellAmulet()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showConstructionCape",
		name = "Construction cape",
		description = "The 9 house portal towns (9 locations)",
		position = 0,
		section = capesSection
	)
	default boolean showConstructionCape()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showCraftingCape",
		name = "Crafting cape",
		description = "Crafting Guild",
		position = 1,
		section = capesSection
	)
	default boolean showCraftingCape()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFarmingCape",
		name = "Farming cape",
		description = "Farming Guild",
		position = 2,
		section = capesSection
	)
	default boolean showFarmingCape()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFishingCape",
		name = "Fishing cape",
		description = "Fishing Guild, Otto's Grotto",
		position = 3,
		section = capesSection
	)
	default boolean showFishingCape()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showHunterCape",
		name = "Hunter cape",
		description = "Carnivorous Chinchompa Area, Black Chinchompa Area, Hunter Guild",
		position = 4,
		section = capesSection
	)
	default boolean showHunterCape()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSailingCape",
		name = "Sailing cape",
		description = "The Pandemonium",
		position = 5,
		section = capesSection
	)
	default boolean showSailingCape()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showStrengthCape",
		name = "Strength cape",
		description = "Warriors' Guild",
		position = 6,
		section = capesSection
	)
	default boolean showStrengthCape()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showAchievementCape",
		name = "Achievement diary cape",
		description = "Diary masters across the world (16 locations)",
		position = 7,
		section = capesSection
	)
	default boolean showAchievementCape()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showQuestCape",
		name = "Quest cape",
		description = "Legends' Guild",
		position = 8,
		section = capesSection
	)
	default boolean showQuestCape()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showMusicCape",
		name = "Music cape",
		description = "Falo the Bard",
		position = 9,
		section = capesSection
	)
	default boolean showMusicCape()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showMythicalCape",
		name = "Mythical cape",
		description = "Myth's Guild",
		position = 10,
		section = capesSection
	)
	default boolean showMythicalCape()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showArdougneCloak",
		name = "Ardougne cloak",
		description = "Monastery, Farm",
		position = 0,
		section = diarySection
	)
	default boolean showArdougneCloak()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showDesertAmulet",
		name = "Desert amulet",
		description = "Nardah, Kalphite Cave",
		position = 1,
		section = diarySection
	)
	default boolean showDesertAmulet()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showExplorersRing",
		name = "Explorer's ring",
		description = "Cabbage patch near Falador",
		position = 2,
		section = diarySection
	)
	default boolean showExplorersRing()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFremennikSeaBoots",
		name = "Fremennik sea boots",
		description = "Rellekka",
		position = 3,
		section = diarySection
	)
	default boolean showFremennikSeaBoots()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showKandarinHeadgear",
		name = "Kandarin headgear",
		description = "Sherlock",
		position = 4,
		section = diarySection
	)
	default boolean showKandarinHeadgear()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showKaramjaGloves",
		name = "Karamja gloves",
		description = "Gem Mine (Underground), Duradel",
		position = 5,
		section = diarySection
	)
	default boolean showKaramjaGloves()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showMorytaniaLegs",
		name = "Morytania legs",
		description = "Slime Pit (Underground), Burgh de Rott",
		position = 6,
		section = diarySection
	)
	default boolean showMorytaniaLegs()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showWesternBanner",
		name = "Western banner",
		description = "Piscatoris",
		position = 7,
		section = diarySection
	)
	default boolean showWesternBanner()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showWildernessSword",
		name = "Wilderness sword",
		description = "Fountain of Rune (Wilderness)",
		position = 8,
		section = diarySection
	)
	default boolean showWildernessSword()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRadasBlessing",
		name = "Rada's blessing",
		description = "Kourend Woodland and Mount Karuulm",
		position = 9,
		section = diarySection
	)
	default boolean showRadasBlessing()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFairyRings",
		name = "Fairy rings",
		description = "Every fairy ring, with its code. Using them needs a dramen or lunar staff",
		position = 0,
		section = travelSection
	)
	default boolean showFairyRings()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showMinigames",
		name = "Minigame teleports",
		description = "Destinations in the minigame teleport menu on the Grouping tab",
		position = 1,
		section = travelSection
	)
	default boolean showMinigames()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showScrolls",
		name = "Scrolls and tablets",
		description = "Teleport scrolls such as Digsite, Nardah and Feldip Hills",
		position = 2,
		section = travelSection
	)
	default boolean showScrolls()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showOther",
		name = "Quest and reward items",
		description = "Xeric's Talisman, Ectophial, Drakan's medallion, Enchanted lyre, Pharaoh's sceptre, Royal seed pod, obelisks and other items",
		position = 0,
		section = questItemsSection
	)
	default boolean showOther()
	{
		return true;
	}

	@ConfigItem(
		keyName = "flash",
		name = "Flash",
		description = "Make the icons flash, like the world map key does. Turn off for a steady highlight",
		position = 0,
		section = appearanceSection
	)
	default boolean flash()
	{
		return true;
	}

	@Range(min = 150, max = 2000)
	@Units(Units.MILLISECONDS)
	@ConfigItem(
		keyName = "flashSpeed",
		name = "Flash speed",
		description = "How long each flash lasts. Lower is faster",
		position = 1,
		section = appearanceSection
	)
	default int flashSpeed()
	{
		return 400;
	}

	@ConfigItem(
		keyName = "highlightColour",
		name = "Highlight colour",
		description = "Colour of the glow drawn round each teleport icon",
		position = 2,
		section = appearanceSection
	)
	default Color highlightColour()
	{
		return new Color(255, 230, 0);
	}

	@ConfigItem(
		keyName = "buttonSide",
		name = "Button side",
		description = "Which side of the zoom out button the teleport button sits. Auto puts it on the side away from zoom in",
		position = 3,
		section = appearanceSection
	)
	default ButtonSide buttonSide()
	{
		return ButtonSide.AUTO;
	}

	@ConfigItem(
		keyName = "turnOffOnClose",
		name = "Turn off when map closes",
		description = "Switch the highlight off each time the world map is closed",
		position = 4,
		section = appearanceSection
	)
	default boolean turnOffOnClose()
	{
		return false;
	}
}
