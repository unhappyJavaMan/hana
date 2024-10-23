package com.hana.service.Common;

import com.hana.service.DAO.Entity.SystemConfigEntity;
import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.DAO.Repository.SystemConfigRepository;
import com.hana.service.DAO.Repository.UserRepository;
import com.hana.service.Utils.LogUtils;
import com.hana.service.Utils.Methods;
import com.hana.service.Utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Component
public class AppConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private SystemConfigRepository systemConfigRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

    private LogUtils logger = new LogUtils();


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
		createInitialAdminUser();
		configLoginTokenKey();
    }

	public void createInitialAdminUser() {
		if (userRepository.count() == 0) {
			logger.info("No users found. Creating initial admin user...");
			UserEntity adminUser = new UserEntity();
			adminUser.setAccount("admin");
			adminUser.setPassword(passwordEncoder.encode("admin"));
			adminUser.setNickName("admin");
			adminUser.setPhone("0000000000");
			adminUser.setStatus(Const.USER_STATUS_ACTIVE);
			adminUser.setEmail("admin@admin.com");
			adminUser.setCreateDate(TimeUtils.getNowUTCLocalDateTime());

			userRepository.save(adminUser);
			logger.info("Initial admin user created successfully.");
		} else {
			logger.info("Users already exist. Skipping initial admin user creation.");
		}
	}

    public void configLoginTokenKey() {
        SystemConfigEntity systemConfig = systemConfigRepository.findBySystemKey(Const.STRING_LOGIN_TOKEN_KEY);
        String value = null;
        if (systemConfig == null || systemConfig.getSystemValue() == null || systemConfig.getSystemValue().isBlank()) {
            logger.info("LoginTokenKey building...");
            String privateKeyStr = Methods.generateHS512Key();

            SystemConfigEntity systemConfigEntity = new SystemConfigEntity();
            systemConfigEntity.setSystemKey(Const.STRING_LOGIN_TOKEN_KEY);
            systemConfigEntity.setSystemValue(privateKeyStr);
            systemConfigRepository.save(systemConfigEntity);
            value = privateKeyStr;
        } else {
            value = systemConfig.getSystemValue();
            logger.info("LoginTokenKey already exist");
        }
        logger.info("LoginTokenKey init success");
        Const.secretKeyForJWT = value;
    }

}
