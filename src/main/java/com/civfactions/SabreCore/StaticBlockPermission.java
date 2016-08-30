package com.civfactions.SabreCore;

import com.civfactions.SabreApi.BlockPermission;
import com.civfactions.SabreApi.SabrePlayer;

class StaticBlockPermission implements BlockPermission {

	private final boolean access;
	private final boolean modify;
	
	StaticBlockPermission(boolean access, boolean modify) {
		this.access = access;
		this.modify = modify;
	}
	
	@Override
	public boolean canPlayerAccess(SabrePlayer p) {
		return access;
	}

	@Override
	public boolean canPlayerModify(SabrePlayer p) {
		return modify;
	}

}
