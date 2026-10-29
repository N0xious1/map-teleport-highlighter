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

import lombok.Getter;

/**
 * Teleport items that have their own tick box in the settings.
 * The config key matches a method in {@link TeleportHighlighterConfig}.
 */
@Getter
enum ItemToggle
{
	// jewellery
	AMULET_OF_GLORY(Group.JEWELLERY, "Amulet of Glory", "showAmuletOfGlory"),
	RING_OF_DUELING(Group.JEWELLERY, "Ring of Dueling", "showRingOfDueling"),
	GAMES_NECKLACE(Group.JEWELLERY, "Games Necklace", "showGamesNecklace"),
	SKILLS_NECKLACE(Group.JEWELLERY, "Skills Necklace", "showSkillsNecklace"),
	COMBAT_BRACELET(Group.JEWELLERY, "Combat Bracelet", "showCombatBracelet"),
	RING_OF_WEALTH(Group.JEWELLERY, "Ring of Wealth", "showRingOfWealth"),
	SLAYER_RING(Group.JEWELLERY, "Slayer Ring", "showSlayerRing"),
	DIGSITE_PENDANT(Group.JEWELLERY, "Digsite Pendant", "showDigsitePendant"),
	BURNING_AMULET(Group.JEWELLERY, "Burning Amulet", "showBurningAmulet"),
	NECKLACE_OF_PASSAGE(Group.JEWELLERY, "Necklace of Passage", "showNecklaceOfPassage"),
	SAILORS_AMULET(Group.JEWELLERY, "Sailors' Amulet", "showSailorsAmulet"),
	GIANTSOUL_AMULET(Group.JEWELLERY, "Giantsoul Amulet", "showGiantsoulAmulet"),
	PENDANT_OF_ATES(Group.JEWELLERY, "Pendant of Ates", "showPendantOfAtes"),
	COWBELL_AMULET(Group.JEWELLERY, "Cowbell Amulet", "showCowbellAmulet"),

	// capes
	CONSTRUCTION_CAPE(Group.CAPE, "Construction Cape", "showConstructionCape"),
	CRAFTING_CAPE(Group.CAPE, "Crafting Cape", "showCraftingCape"),
	FARMING_CAPE(Group.CAPE, "Farming Cape", "showFarmingCape"),
	FISHING_CAPE(Group.CAPE, "Fishing Cape", "showFishingCape"),
	HUNTER_CAPE(Group.CAPE, "Hunter Cape", "showHunterCape"),
	SAILING_CAPE(Group.CAPE, "Sailing Cape", "showSailingCape"),
	STRENGTH_CAPE(Group.CAPE, "Strength Cape", "showStrengthCape"),
	ACHIEVEMENT_CAPE(Group.CAPE, "Achievement Cape", "showAchievementCape"),
	QUEST_CAPE(Group.CAPE, "Quest Cape", "showQuestCape"),
	MUSIC_CAPE(Group.CAPE, "Music Cape", "showMusicCape"),
	MYTHICAL_CAPE(Group.CAPE, "Mythical Cape", "showMythicalCape"),

	// diary
	ARDOUGNE_CLOAK(Group.DIARY, "Ardougne Cloak", "showArdougneCloak"),
	DESERT_AMULET(Group.DIARY, "Desert Amulet", "showDesertAmulet"),
	EXPLORER_S_RING(Group.DIARY, "Explorer's Ring", "showExplorersRing"),
	FREMENNIK_SEA_BOOTS(Group.DIARY, "Fremennik Sea Boots", "showFremennikSeaBoots"),
	KANDARIN_HEADGEAR(Group.DIARY, "Kandarin Headgear", "showKandarinHeadgear"),
	KARAMJA_GLOVES(Group.DIARY, "Karamja Gloves", "showKaramjaGloves"),
	MORYTANIA_LEGS(Group.DIARY, "Morytania Legs", "showMorytaniaLegs"),
	WESTERN_BANNER(Group.DIARY, "Western Banner", "showWesternBanner"),
	WILDERNESS_SWORD(Group.DIARY, "Wilderness Sword", "showWildernessSword"),
	RADA_S_BLESSING(Group.DIARY, "Rada's Blessing", "showRadasBlessing");

	enum Group
	{
		JEWELLERY("Jewellery"),
		CAPE("Cape"),
		DIARY("Achievement diary reward");

		@Getter
		private final String label;

		Group(String label)
		{
			this.label = label;
		}
	}

	private final Group group;
	/** The item name used in TeleportLocationData. */
	private final String itemName;
	private final String configKey;

	ItemToggle(Group group, String itemName, String configKey)
	{
		this.group = group;
		this.itemName = itemName;
		this.configKey = configKey;
	}

	/** The toggle for a teleport item, or null if the item has no tick box of its own. */
	static ItemToggle fromItem(String item)
	{
		for (ItemToggle toggle : values())
		{
			if (toggle.itemName.equals(item))
			{
				return toggle;
			}
		}
		return null;
	}
}
