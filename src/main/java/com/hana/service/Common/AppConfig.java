package com.hana.service.Common;

import com.hana.service.DAO.Entity.SystemConfigEntity;
import com.hana.service.DAO.Repository.SystemConfigRepository;
import com.hana.service.Utils.LogUtils;
import com.hana.service.Utils.Methods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class AppConfig implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private SystemConfigRepository systemConfigRepository;

	private LogUtils logger = new LogUtils();

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		configLoginTokenKey();
	}

	public void configLoginTokenKey() {
		SystemConfigEntity systemConfig = systemConfigRepository.findBySystemKey(Const.STRING_LOGIN_TOKEN_KEY);
		String value = null;
		if (systemConfig == null || systemConfig.getSystemValue() == null || systemConfig.getSystemValue().isBlank()){
			logger.info("LoginTokenKey building...");
			String privateKeyStr = Methods.generateHS512Key();

			SystemConfigEntity systemConfigEntity = new SystemConfigEntity();
			systemConfigEntity.setSystemKey(Const.STRING_LOGIN_TOKEN_KEY);
			systemConfigEntity.setSystemValue(privateKeyStr);
			systemConfigRepository.save(systemConfigEntity);
			value = privateKeyStr;
		}else {
			value = systemConfig.getSystemValue();
			logger.info("LoginTokenKey already exist");
		}
		logger.info("LoginTokenKey init success");
		Const.secretKeyForJWT = value;
	}

}
