package com.obal.cache.aop;

import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.cache.CacheManager;
import com.obal.core.EntryKey;
import com.obal.meta.EntityAttr;

@Aspect
public abstract class CacheAspect {

	public static Logger LOGGER = LoggerFactory.getLogger(CacheAspect.class);
	
	@Pointcut
    abstract void cacheAfterPut();
	    
    @After("cacheAfterPut()")
    public void afterPut(JoinPoint jp) throws Throwable{

    	Object putValue = null;
    	String key = null;
    	String entity = null;
    	String attr = null;
    	String value = null;
    	
    	CodeSignature codesign = (CodeSignature) jp.getStaticPart().getSignature();
    	String[] names = codesign.getParameterNames();
//    	Class<?>[] types = codesign.getParameterTypes();
    	Object[] values = jp.getArgs();
    	
    	Object m = jp.getTarget();
    	Method jpmethod = m.getClass().getDeclaredMethod(codesign.getName(), codesign.getParameterTypes());
    	CacheablePut cp = jpmethod.getAnnotation(CacheablePut.class);
    	if(null == cp){
    		LOGGER.warn("Method{}-{} not have CacheablePut annotation, ignore..", jp.getClass().getName(),codesign.getName());
    		return ;
    	}

    	key = cp.entrykey();
    	entity = cp.entity();
    	attr = cp.attr();
    	value = cp.value();
    	// parse attribute string
    	int index = ArrayUtils.indexOf(names, value);
    	if(index > -1){
    		
    		putValue = values[index];
    		if(putValue instanceof EntryKey){
    			CacheManager.getInstance().cachePut((EntryKey)putValue);
    			return;
	    	}
    	}else{
    		
    		LOGGER.warn("The value:{} not exist in parameters.",value);
    		return;
    	}

    	// parse the key value if it is EntryKey extract key and entity string.
    	index = ArrayUtils.indexOf(names, key);
    	Object val = null;
    	if(index > -1){
	    	val = values[index];
	    	if(val instanceof String){
	    		key = (String)val;
	    	}else if(val instanceof EntryKey){
	    		key = ((EntryKey)val).getKey();    		
	    		entity = StringUtils.isBlank(entity)?((EntryKey)val).getEntityName():entity;
	    	}else{
	    		
	    		LOGGER.warn("The key is assigned a unidentiable parameter:{}",key);
	    	}
    	}else{
    		
    		LOGGER.warn("The key:{} not exist in parameters.",key);
    	}
    	// parse entity string
    	index = ArrayUtils.indexOf(names, entity);
    	if(index > -1){
	    	val = values[index];
	    	if(val instanceof String){
	    		entity = ( val == null )? entity:(String)val;
	    	}else{
	    		
	    		LOGGER.warn("The entity is assigned a unidentiable parameter:{}",entity);
	    	}
    	}else{
    		
    		LOGGER.warn("The entity:{} not exist in parameters.",entity);
    	}
    	
    	// parse attribute string
    	index = ArrayUtils.indexOf(names, attr);
    	if(index > -1){
	    	val = values[index];
	    	if(val instanceof String){
	    		attr = (String)val;
	    	}else if(val instanceof EntityAttr){
	    		
	    		attr = ((EntityAttr)val).getAttrName();
	    		entity = entity == null? ((EntityAttr)val).getEntityName():entity;
	    	}else{
	    		
	    		LOGGER.warn("The entity is assigned a unidentiable parameter:{}",attr);
	    	}
    	}else{
    		
    		LOGGER.warn("The attribute:{} not exist in parameters.",attr);
    	}
    	

    	if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(entity)
    			&& StringUtils.isNotBlank(attr)){
    		
    		CacheManager.getInstance().cachePutAttr(new EntryKey(entity, key), attr,putValue);

    	}else{
    		
    		LOGGER.warn("The annotation parameters not engouth:{}-{}-{}.", new String[]{key,entity,attr});
    		
    	}

    }

	@Pointcut
    abstract void cacheAroundGet();
	
	/**
	 * Around get operation to intercept the request for entry via key parameter.
	 **/
    @Around("cacheAroundGet()")
    public Object aroundGet(ProceedingJoinPoint jp) throws Throwable{
    	
    	Object returnVal = null;
    	String key = null;
    	String entity = null;
    	String attr = null;
    	
    	CodeSignature codesign = (CodeSignature) jp.getStaticPart().getSignature();
    	String[] names = codesign.getParameterNames();
//    	Class<?>[] types = codesign.getParameterTypes();
    	Object[] values = jp.getArgs();
    	
    	Object m = jp.getTarget();
    	Method jpmethod = m.getClass().getDeclaredMethod(codesign.getName(), codesign.getParameterTypes());
    	CacheableGet cg = jpmethod.getAnnotation(CacheableGet.class);
    	if(null == cg){
    		LOGGER.warn("Method{}-{} not have CacheableGet annotation, ignore..", jp.getClass().getName(),codesign.getName());
    		return jp.proceed();
    	}

    	key = cg.entrykey();
    	entity = cg.entity();
    	attr = cg.attr();
    	// parse the key value if it is EntryKey extract key and entity string.
    	int index = ArrayUtils.indexOf(names, key);
    	Object val = null;
    	if(index > -1){
	    	val = values[index];
	    	if(val instanceof String){
	    		key = (String)val;
	    	}else if(val instanceof EntryKey){
	    		key = ((EntryKey)val).getKey();    		
	    		entity = StringUtils.isBlank(entity)?((EntryKey)val).getEntityName():entity;
	    	}else{
	    		
	    		LOGGER.warn("The key is assigned a unidentiable parameter:{}",key);
	    	}
    	}else{
    		
    		LOGGER.warn("The key:{} not exist in parameters.",key);
    	}
    	// parse entity string
    	index = ArrayUtils.indexOf(names, entity);
    	if(index > -1){
	    	val = values[index];
	    	if(val instanceof String){
	    		entity = ( val == null )? entity:(String)val;
	    	}else{
	    		
	    		LOGGER.warn("The entity is assigned a unidentiable parameter:{}",entity);
	    	}
    	}else{
    		
    		LOGGER.warn("The entity:{} not exist in parameters.",entity);
    	}
    	
    	// parse attribute string
    	index = ArrayUtils.indexOf(names, attr);
    	if(index > -1){
	    	val = values[index];
	    	if(val instanceof String){
	    		attr = (String)val;
	    	}else if(val instanceof EntityAttr){
	    		
	    		attr = ((EntityAttr)val).getAttrName();
	    		entity = entity == null? ((EntityAttr)val).getEntityName():entity;
	    	}else{
	    		
	    		LOGGER.warn("The entity is assigned a unidentiable parameter:{}",attr);
	    	}
    	}else{
    		
    		LOGGER.warn("The attribute:{} not exist in parameters.",attr);
    	}
    	
    	if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(entity)){
    		
    		returnVal = CacheManager.getInstance().cacheGet(entity, key);
    		
    		if(returnVal == null){
    			
    			returnVal = jp.proceed();
    		}
    		
    	}else if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(entity)
    			&& StringUtils.isNotBlank(attr)){
    		
    		returnVal = CacheManager.getInstance().cacheGetAttr(entity, key, attr);
    		if(returnVal == null){
    			
    			returnVal = jp.proceed();
    		}
    	}

    	return returnVal;
    	
    }
    
	@Pointcut
    abstract void cacheAfterDel();
	
    @After("cacheAfterDel()")
    public void afterDel(JoinPoint jp) {
    	System.out.println("--after del");
    }
}
