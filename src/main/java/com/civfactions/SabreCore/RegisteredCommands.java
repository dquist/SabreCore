package com.civfactions.SabreCore;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.civfactions.SabreApi.SabreCommand;

class RegisteredCommands implements Set<SabreCommand> {

	private final HashSet<SabreCommand> commands = new HashSet<SabreCommand>();
	
	public RegisteredCommands() { }
	
	@Override
	public int size() {
		return commands.size();
	}

	@Override
	public boolean isEmpty() {
		return commands.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return commands.contains(o);
	}

	@Override
	public Iterator<SabreCommand> iterator() {
		return commands.iterator();
	}

	@Override
	public Object[] toArray() {
		return commands.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return commands.toArray(a);
	}

	@Override
	public boolean add(SabreCommand e) {
		return commands.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return commands.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return commands.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends SabreCommand> c) {
		return commands.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return commands.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return commands.removeAll(c);
	}

	@Override
	public void clear() {
		commands.clear();
	}
}
