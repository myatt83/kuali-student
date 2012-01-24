package org.kuali.student.admin.messages.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.google.common.collect.MapMaker;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.student.common.dto.ContextInfo;
import org.kuali.student.common.dto.LocaleInfo;
import org.kuali.student.common.dto.StatusInfo;
import org.kuali.student.common.exceptions.*;
import org.kuali.student.common.messages.dto.*;
import org.kuali.student.common.messages.service.MessageService;
import org.kuali.student.core.enumerationmanagement.bo.EnumeratedValue;
import org.kuali.student.core.messages.bo.MessageEntity;
import org.springframework.beans.factory.InitializingBean;

@WebService(endpointInterface = "org.kuali.student.common.messages.service.MessageService", serviceName = "MessageService", portName = "MessageService", targetNamespace = "http://student.kuali.org/wsdl/messages")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class MessageServiceImpl implements MessageService, InitializingBean {

	protected boolean cachingEnabled = false;
	protected int msgsCacheMaxSize = 20;
	protected int msgsCacheMaxAgeSeconds = 90;
	//protected Map<String,MaxAgeSoftReference<MessageList>> msgsCache;
	protected Map<String,MessageList> msgsCache;

    private BusinessObjectService businessObjectService;

    @Override
    public StatusInfo addMessage(@WebParam(name = "localeInfo") LocaleInfo localeInfo, @WebParam(name = "messageGroupKey") String messageGroupKey, @WebParam(name = "messageInfo") MessageInfo messageInfo, @WebParam(name = "contextInfo") ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        // TODO KSCM-211
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MessageInfo updateMessage(LocaleInfo localeKey, String messageGroupKey, String messageKey, MessageInfo messageInfo, @WebParam(name = "contextInfo") ContextInfo contextInfo) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public MessageInfo addMessage(Message messageInfo) {
        MessageEntity entity = toMessageEntity(messageInfo);
        getBusinessObjectService().save(entity);

        return toMessage(entity);
    }



    @Override
    public List<LocaleInfo> getLocales(@WebParam(name = "contextInfo") ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<String> keyList = new ArrayList<String>();
        LocaleInfo tempLocalInfo;
        List<LocaleInfo> localeKeyList = new ArrayList<LocaleInfo>();
        Map<String, Object> criteria = new HashMap<String,Object>();

        criteria.put(EnumeratedValue.ENUMERATION_KEY, MessageEntity.LOCALE_ENUMERATION);
        BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
        Collection<EnumeratedValue> values = boService.findMatching(EnumeratedValue.class, criteria);

        Iterator<EnumeratedValue> iterator = values.iterator();
        while(iterator.hasNext()) {
            EnumeratedValue value = iterator.next();
            tempLocalInfo = new LocaleInfo();
            tempLocalInfo.setLocaleLanguage(value.getCode());
            localeKeyList.add(tempLocalInfo);
        }

        return localeKeyList;
    }



    @Override
    public MessageInfo getMessage(@WebParam(name = "localeInfo") LocaleInfo localeInfo, @WebParam(name = "messageGroupKey") String messageGroupKey, @WebParam(name = "messageKey") String messageKey, @WebParam(name = "contextInfo") ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("locale", localeInfo.getLocaleLanguage());
        queryMap.put("groupName", messageGroupKey);
        queryMap.put("messageId", messageKey);
        
        MessageEntity entity = (MessageEntity) getBusinessObjectService().findByPrimaryKey(MessageEntity.class, queryMap);
        
        return toMessage(entity);
    }





    @Override
    public StatusInfo deleteMessage(@WebParam(name = "localeInfo") LocaleInfo localeInfo, @WebParam(name = "messageKey") String messageKey, @WebParam(name = "contextInfo") ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        // TODO KSCM-211
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public List<String> getMessageGroupKeys(@WebParam(name = "contextInfo") ContextInfo contextInfo) throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        //TODO KSCM-211
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @SuppressWarnings("unchecked")
    public MessageGroupKeyList getMessageGroups() {
        List<String> keyList = new ArrayList<String>();
        Map<String, Object> criteria = new HashMap<String,Object>();
        
        criteria.put(EnumeratedValue.ENUMERATION_KEY, MessageEntity.GROUP_NAME_ENUMERATION);
        BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
        Collection<EnumeratedValue> values = boService.findMatching(EnumeratedValue.class, criteria);
        
        Iterator<EnumeratedValue> iterator = values.iterator(); 
        while(iterator.hasNext()) {
            EnumeratedValue value = iterator.next();
            keyList.add(value.getCode());
        }
    	
    	MessageGroupKeyList messageGroupKeyList = new MessageGroupKeyList();
    	messageGroupKeyList.setMessageGroupKeys(keyList);
    	
        return messageGroupKeyList;
    }


    @Override
    public List<MessageInfo> getMessages(@WebParam(name = "localeInfo") LocaleInfo localeInfo, @WebParam(name = "messageGroupKey") String messageGroupKey, @WebParam(name = "contextInfo") ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        if(cachingEnabled){
            return (List<MessageInfo>) msgsCache.get("localeKey=" + localeInfo.getLocaleLanguage()+ ", messageGroupKey="+messageGroupKey);
        }

        Map<String,String> fieldValues = new HashMap<String,String>();
        fieldValues.put("locale", localeInfo.getLocaleLanguage());
        fieldValues.put("groupName", messageGroupKey);

        List<MessageEntity> messageEntityList = (List<MessageEntity>) getBusinessObjectService().findMatching(MessageEntity.class, fieldValues);

        List<MessageInfo> messages = new ArrayList<MessageInfo>();

        for(MessageEntity messageEntity: messageEntityList){
         messages.add(toMessage(messageEntity));
        }

        MessageList messageList = new MessageList();
        messageList.setMessages(messages);

        if(cachingEnabled){
            msgsCache.put("localeKey="+localeInfo.getLocaleLanguage()+", messageGroupKey="+messageGroupKey, messageList );
        }

        return (List<MessageInfo>) messageList;
    }

    @Override
    public List<MessageInfo> getMessagesByGroups(@WebParam(name = "localeInfo") LocaleInfo localeInfo, @WebParam(name = "messageGroupKeys") List<String> messageGroupKeys, @WebParam(name = "contextInfo") ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
    	
    	List<MessageInfo> messages = new ArrayList<MessageInfo>();
    	
    	for(String messageGroupKey: messageGroupKeys){
    		messages.addAll(getMessages(localeInfo, messageGroupKey, contextInfo));
    	}
    	
    	MessageList messageList = new MessageList();
    	messageList.setMessages(messages);
    	
    	return (List<MessageInfo>) messageList;

    }

    @Override
    public MessageInfo updateMessage(@WebParam(name = "localeInfo") LocaleInfo localeInfo, @WebParam(name = "messageKey") String messageKey, @WebParam(name = "messageInfo") MessageInfo messageInfo, @WebParam(name = "contextInfo") ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, ReadOnlyException, VersionMismatchException {
        // TODO KSCM-211
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public MessageInfo updateMessage(LocaleInfo localeInfo, String messageGroupKey, String messageKey, Message messageInfo, @WebParam(name = "contextInfo") ContextInfo contextInfo) {
        
    	Map<String,String> primaryKeys = new HashMap<String,String>();
    	primaryKeys.put("locale", localeInfo.getLocaleLanguage());
    	primaryKeys.put("groupName", messageGroupKey);
    	primaryKeys.put("messageId", messageKey);
    	
    	MessageEntity entity = (MessageEntity) getBusinessObjectService().findByPrimaryKey(MessageEntity.class, primaryKeys);
    	
    	entity.setMessageId(messageInfo.getId());
    	entity.setLocale(messageInfo.getLocale());
    	entity.setGroupName(messageInfo.getGroupName());
    	entity.setValue(messageInfo.getValue());
    	
    	getBusinessObjectService().save(entity);
    	
    	return toMessage(entity);
    	
    }

    protected MessageEntity toMessageEntity(Message message) {
        MessageEntity result = new MessageEntity();

        result.setMessageId(message.getId());
        result.setLocale(message.getLocale());
        result.setGroupName(message.getGroupName());
        result.setValue(message.getValue());

        return result;
    }

    protected MessageInfo toMessage(MessageEntity entity) {
        MessageInfo result = new MessageInfo();

        result.setGroupName(entity.getGroupName());
        result.setKey(entity.getId());
        result.setLocale(entity.getLocale());
        result.setValue(entity.getValue());

        return result;
    }

    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		if(cachingEnabled){
			msgsCache = new MapMaker().expireAfterAccess(msgsCacheMaxAgeSeconds, TimeUnit.SECONDS).maximumSize(msgsCacheMaxSize).softValues().makeMap();
		}
	}

	public void setCachingEnabled(boolean cachingEnabled) {
		this.cachingEnabled = cachingEnabled;
	}



}
