package com.hana.service.Utils;

import com.hana.service.Common.Const;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
;
public class LogUtils implements StatementInspector {
	private static final Logger sqlLogger = LoggerFactory.getLogger("HibernateSQL");
	private Logger logger;;
	private boolean logSwitch = true;

	public LogUtils(String context) {
		logger = LoggerFactory.getLogger(context);
	}

	public LogUtils() {
		String context;
		// 取得調用這個 LogUtils 的類別名稱
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (stackTrace.length > 2) {
			context = stackTrace[2].getClassName();
		} else {
			context = "Unknown";
		}
		logger = LoggerFactory.getLogger(context);
	}

	public void info(String transactionInvoice, String log) {
		if (logSwitch) {
			logger.info("(" + transactionInvoice + ")" + log);
		}
	}

	public void info(HttpServletRequest req, String log) {
		if (logSwitch) {
			logger.info("(" + req.getAttribute(Const.REQUEST_ID).toString() + ")" + log);
		}
	}

	public void info(String log) {
		if (logSwitch) {
			logger.info(log);
		}
	}

	public void error(Exception e) {
		if (logSwitch) {
			logger.info("-------------------------------------------------------------------------");
			logger.error(String.format("%s", e.getMessage()), e);
			logger.info("-------------------------------------------------------------------------");
		}
	}

	@Override
	public String inspect(String sql) {
		if (logSwitch) {
			sqlLogger.info("Hibernate SQL: {}", sql);
		}
		return sql;
	}
	public static void enableSqlLogging() {
		System.setProperty("hibernate.session_factory.statement_inspector", LogUtils.class.getName());
	}
}
