package com.amituofo.cmdlet.component;

import com.amituofo.cmdlet.core.ArgumentParseException;
import com.amituofo.cmdlet.core.Arguments;
import com.amituofo.cmdlet.core.CommandArguments;
import com.amituofo.common.kit.remote.RMIRegistry;
import com.amituofo.common.util.StringUtils;

public abstract class TaskCommandArguments extends CommandArguments {

	private String processServiceHost;

	public TaskCommandArguments(String version) {
		super(version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void inject(Arguments args) throws ArgumentParseException {
		processServiceHost = args.getValue("processServiceHost", 0);
	}

	public String getProcessServiceHost() {
		if (StringUtils.isEmpty(processServiceHost)) {
			processServiceHost = "127.0.0.1:" + RMIRegistry.DEFAULT_REGISTRY_PORT;
		}
//		if (processServiceHost.indexOf(':') == -1) {
//			processServiceHost = processServiceHost + ":" + RMIRegistry.DEFAULT_PORT;
//		}
		return processServiceHost;
	}

}
