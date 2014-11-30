package com.hundsun.hsccbp.pearls;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String log = "sf Reactor Summary: [INFO] [INFO] etrading .............[ERROR] COMPILATION ERROR............................. SUCCESS [2.781s] [INFO] etrading-base ..................................... SUCCESS [0.047s] [INFO] fund-common ....................................... SUCCESS [1:35.907s] [INFO] fund-app .......................................... SUCCESS [1:38.813s] [INFO] fund-capital ...................................... SUCCESS [2:45.875s] [INFO] fund-service ...................................... FAILURE [4.953s] [INFO] fund-deprecated ................................... SKIPPED [INFO] fund-config ....................................... SKIPPED [INFO] etrading-comm ..................................... SKIPPED [INFO] etrading-web ...................................... SKIPPED [INFO] ------------------------------------------------------------------------ [INFO] BUILD FAILURE [INFO] ------------------------------------------------------------------------ [INFO] Total time: 6:13.562s [INFO] Finished at: Sun Sep 21 23:12:33 CST 2014 [INFO] Final Memory: 28M/184M [INFO] no threshold has been exceeded [FINDBUGS] Plug-in Result: Failed - 58 warnings of priority Normal Priority exceed the threshold of 56 by 2 An attempt to send an e-mail to empty list of recipients, ";
		Pattern p = Pattern.compile("^*Reactor\\sSummary | ^*\\[ERROR\\]\\sCOMPILATION\\sERROR");
		//Pattern p = Pattern.compile("^*Reactor Summary");
		//String log = "no threshold has been exceeded [FINDBUGS] Plug-in Result: Failed - 58 warnings of priority Normal Priority exceed the threshold of 56 by 2 An attempt to send an e-mail to empty list of recipients, ";
		//Pattern p = Pattern.compile("^*\\[FINDBUGS\\]\\sPlug\\-in\\sResult\\:\\sFailed");
				
		Matcher m = p.matcher(log);
		while (m.find()){
			System.out.println(m.group());
		}


	}

}
