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

import net.runelite.api.coords.WorldPoint;

/**
 * Works out whether a map position is in the Wilderness, so those teleports
 * can be hidden from new players or carry a warning.
 */
final class Wilderness
{
	// Above ground: from the Wilderness ditch north to the top of the map
	private static final int SURFACE_MIN_X = 2944;
	private static final int SURFACE_MAX_X = 3391;
	private static final int SURFACE_MIN_Y = 3523;
	private static final int SURFACE_MAX_Y = 3967;

	// Underground: Revenant Caves, Wilderness God Wars Dungeon, Slayer Cave and the other Wilderness dungeons
	private static final int UNDERGROUND_MIN_X = 2944;
	private static final int UNDERGROUND_MAX_X = 3455;
	private static final int UNDERGROUND_MIN_Y = 9918;
	private static final int UNDERGROUND_MAX_Y = 10367;

	// Ferox Enclave is a safe zone inside the Wilderness
	private static final int FEROX_MIN_X = 3123;
	private static final int FEROX_MAX_X = 3156;
	private static final int FEROX_MIN_Y = 3617;
	private static final int FEROX_MAX_Y = 3646;

	private Wilderness()
	{
	}

	static boolean contains(WorldPoint point)
	{
		final int x = point.getX();
		final int y = point.getY();

		if (inBox(x, y, FEROX_MIN_X, FEROX_MAX_X, FEROX_MIN_Y, FEROX_MAX_Y))
		{
			return false;
		}

		return inBox(x, y, SURFACE_MIN_X, SURFACE_MAX_X, SURFACE_MIN_Y, SURFACE_MAX_Y)
			|| inBox(x, y, UNDERGROUND_MIN_X, UNDERGROUND_MAX_X, UNDERGROUND_MIN_Y, UNDERGROUND_MAX_Y);
	}

	private static boolean inBox(int x, int y, int minX, int maxX, int minY, int maxY)
	{
		return x >= minX && x <= maxX && y >= minY && y <= maxY;
	}
}
