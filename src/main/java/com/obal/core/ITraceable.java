package com.obal.core;

import java.util.Date;

public interface ITraceable {
	
	public String getCreator() ;

	public void setCreator(String creator) ;

	public String getModifier() ;

	public void setModifier(String modifier) ;

	public Date getNewCreate() ;

	public void setNewCreate(Date newCreate) ;

	public Date getLastMofify() ;

	public void setLastMofify(Date lastMofify) ;
}
