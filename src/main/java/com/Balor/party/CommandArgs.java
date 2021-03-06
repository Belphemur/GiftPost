/*
 * Copyright (C) 2010 sk89q <http://www.sk89q.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.Balor.party;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class CommandArgs implements Iterable<String> {
	protected final List<String> parsedArgs;
	protected final Map<Character, String> valueFlags = new HashMap<Character, String>();
	protected final Set<Character> booleanFlags = new HashSet<Character>();
	public int length;

	public CommandArgs(String args) {
		this(args.split(" "));
	}

	/**
		 * 
		 */
	public CommandArgs(String[] args) {
		List<Integer> argIndexList = new ArrayList<Integer>(args.length);
		List<String> argList = new ArrayList<String>(args.length);
		for (int i = 0; i < args.length; ++i) {
			String arg = args[i];
			if (arg.length() == 0) {
				continue;
			}

			argIndexList.add(i);

			switch (arg.charAt(0)) {
			case '\'':
			case '"':
				final StringBuilder build = new StringBuilder();
				final char quotedChar = arg.charAt(0);

				int endIndex;
				for (endIndex = i; endIndex < args.length; ++endIndex) {
					final String arg2 = args[endIndex];
					if (arg2.charAt(arg2.length() - 1) == quotedChar) {
						if (endIndex != i)
							build.append(' ');
						build.append(arg2.substring(endIndex == i ? 1 : 0, arg2.length() - 1));
						break;
					} else if (endIndex == i) {
						build.append(arg2.substring(1));
					} else {
						build.append(' ').append(arg2);
					}
				}

				if (endIndex < args.length) {
					arg = build.toString();
					i = endIndex;
				}

				// In case there is an empty quoted string
				if (arg.length() == 0) {
					continue;
				}
				// else raise exception about hanging quotes?
			}
			argList.add(arg);
		}
		// Then flags

		List<Integer> originalArgIndices = new ArrayList<Integer>(argIndexList.size());
		this.parsedArgs = new ArrayList<String>(argList.size());

		for (int nextArg = 0; nextArg < argList.size();) {
			// Fetch argument
			String arg = argList.get(nextArg++);

			if (arg.charAt(0) == '-' && arg.length() > 1 && arg.matches("^-[a-zA-Z]+$")) {
				for (int i = 1; i < arg.length(); ++i) {
					char flagName = arg.charAt(i);
					if (this.valueFlags.containsKey(flagName)) {
						continue;
					}

					if (nextArg >= argList.size())
						this.booleanFlags.add(flagName);
					else
						this.valueFlags.put(flagName, argList.get(nextArg));

				}
				continue;
			}
			originalArgIndices.add(argIndexList.get(nextArg - 1));
			parsedArgs.add(arg);

		}
		this.length = parsedArgs.size();
	}

	public String getString(int index) {
		try {
			return parsedArgs.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}

	}

	/**
	 * Try to parse the argument to an int
	 * 
	 * @param index
	 * @return
	 * @throws NumberFormatException
	 */
	public int getInt(int index) throws NumberFormatException {
		return Integer.parseInt(getString(index));
	}

	/**
	 * Try to parse the argument to a float
	 * 
	 * @param index
	 * @return
	 * @throws NumberFormatException
	 */
	public float getFloat(int index) throws NumberFormatException {
		return Float.parseFloat(getString(index));
	}

	/**
	 * Try to parse the argument to a double
	 * 
	 * @param index
	 * @return
	 * @throws NumberFormatException
	 */
	public double getDouble(int index) throws NumberFormatException {
		return Double.parseDouble(getString(index));
	}

	/**
	 * Try to parse the argument to a long
	 * 
	 * @param index
	 * @return
	 * @throws NumberFormatException
	 */
	public long getLong(int index) throws NumberFormatException {
		return Long.parseLong(getString(index));
	}

	/**
	 * Check if the arguments contain the wanted flagF
	 * 
	 * @param ch
	 *            flag to be searched
	 * @return true if found.
	 */
	public boolean hasFlag(char ch) {
		return booleanFlags.contains(ch) || valueFlags.containsKey(ch);
	}

	/**
	 * Get the Value Flag, and remove the flag from the normal arguments.
	 * 
	 * @param ch
	 *            flag to look for.
	 * @return null if not found else the value of the flag
	 */
	public String getValueFlag(char ch) {
		String result = valueFlags.get(ch);
		if (result == null)
			return null;
		if (parsedArgs.remove(result))
			length--;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Arrays.toString(parsedArgs.toArray(new String[] {})) + " BoolFlags : "
				+ Arrays.toString(booleanFlags.toArray(new Character[] {})) + " ValFlags : "
				+ (valueFlags.isEmpty() ? "[]" : "[" + valueFlagsToString() + "]");
	}

	private String valueFlagsToString() {
		StringBuffer buffer = new StringBuffer();
		for (Entry<Character, String> entry : valueFlags.entrySet()) {
			buffer.append(entry.getKey()).append("->").append(entry.getValue()).append(", ");
		}
		buffer.deleteCharAt(buffer.length() - 1).deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<String> iterator() {
		return parsedArgs.iterator();
	}
}
