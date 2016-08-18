package com.civfactions.SabreCore;

import java.util.Collection;

import com.civfactions.SabreApi.data.DataAccess;

public interface DataStorage extends DataAccess {
	
	Collection<SabrePlayer> playersReadAll();

}
