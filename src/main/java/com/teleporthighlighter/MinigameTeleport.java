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
import net.runelite.api.coords.WorldPoint;

/**
 * Destinations in the minigame teleport menu (Grouping tab).
 * Locations are the minigame positions used by RuneLite's World Map plugin.
 * Requirements are from the OSRS Wiki "Minigame teleports" page.
 */
@Getter
enum MinigameTeleport
{
	BARBARIAN_ASSAULT("Barbarian Assault", "Barbarian Assault tutorial", new WorldPoint(2531, 3569, 0)),
	BURTHORPE_GAMES_ROOM("Burthorpe Games Room", null, new WorldPoint(2900, 3565, 0)),
	CASTLE_WARS("Castle Wars", null, new WorldPoint(2439, 3092, 0)),
	CLAN_WARS("Clan Wars", null, new WorldPoint(3133, 3621, 0)),
	FISHING_TRAWLER("Fishing Trawler", "Fishing level 15", new WorldPoint(2667, 3163, 0)),
	LAST_MAN_STANDING("Last Man Standing", null, new WorldPoint(3138, 3635, 0)),
	MAGE_TRAINING_ARENA("Mage Training Arena", "Talk to the Entrance Guardian first", new WorldPoint(3362, 3318, 0)),
	MASTERING_MIXOLOGY("Mastering Mixology", "Children of the Sun, Herblore level 60", new WorldPoint(1388, 2920, 0)),
	NIGHTMARE_ZONE("Nightmare Zone", "5 quests with boss fights", new WorldPoint(2606, 3115, 0)),
	PEST_CONTROL("Pest Control", "Combat level 40", new WorldPoint(2660, 2637, 0)),
	RAT_PITS_ARDOUGNE("Rat Pits - Ardougne", "Ratcatchers quest", new WorldPoint(2561, 3318, 0)),
	RAT_PITS_VARROCK("Rat Pits - Varrock", "Ratcatchers quest", new WorldPoint(3266, 3400, 0)),
	RAT_PITS_KELDAGRIM("Rat Pits - Keldagrim", "Ratcatchers quest", new WorldPoint(2913, 10188, 0)),
	RAT_PITS_PORT_SARIM("Rat Pits - Port Sarim", "Ratcatchers quest", new WorldPoint(3015, 3232, 0)),
	SHADES_OF_MORTTON("Shades of Mort'ton", "Shades of Mort'ton quest", new WorldPoint(3505, 3315, 0)),
	SORCERESSS_GARDEN("Sorceress's Garden", "Prince Ali Rescue, then talk to Osman", new WorldPoint(3285, 3180, 0)),
	SOUL_WARS("Soul Wars", null, new WorldPoint(2209, 2855, 0)),
	TITHE_FARM("Tithe Farm", "Farming level 34, visit it once", new WorldPoint(1796, 3501, 0)),
	TROUBLE_BREWING("Trouble Brewing", "Cooking level 40, Cabin Fever", new WorldPoint(3811, 3021, 0)),
	TZHAAR_FIGHT_PIT("TzHaar Fight Pit", null, new WorldPoint(2398, 5177, 0));

	static final String ICON_PATH = "icons/minigame_teleport_icon.png";

	private final String name;
	/** What you need before the teleport is unlocked, or null if nothing. */
	private final String requirement;
	private final WorldPoint location;

	MinigameTeleport(String name, String requirement, WorldPoint location)
	{
		this.name = name;
		this.requirement = requirement;
		this.location = location;
	}
}
