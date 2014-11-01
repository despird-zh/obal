package com.obal.core.security;

import java.util.ArrayList;
import java.util.List;
import com.obal.core.util.CoreConstants;

public class EntryAcl {

	List<EntryAce> aces = new ArrayList<EntryAce>();
	
	/**
	 * Get the user aces
	 * @return the entry ace list 
	 **/
	public List<EntryAce> userAces(){
		List<EntryAce> races = new ArrayList<EntryAce>();
		
		for(EntryAce e:aces){
			
			if(CoreConstants.ACE_TYPE_USER.equals(e.type))
				races.add(e);
		}
		
		return races;
	}
	
	/**
	 * Get the role aces
	 * @return the entry ace list 
	 **/
	public List<EntryAce> roleAces(){
		
		List<EntryAce> races = new ArrayList<EntryAce>();
		
		for(EntryAce e:aces){
			
			if(CoreConstants.ACE_TYPE_ROLE.equals(e.type))
				races.add(e);
		}
		
		return races;
	}
	
	/**
	 * Get the group aces
	 * @return the entry ace list 
	 **/
	public List<EntryAce> groupAces(){
		
		List<EntryAce> races = new ArrayList<EntryAce>();
		
		for(EntryAce e:aces){
			
			if(CoreConstants.ACE_TYPE_GROUP.equals(e.type))
				races.add(e);
		}
		
		return races;
	}

}
