package opus.gwt.management.console.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UpdateVersionEventHandler extends EventHandler {
	void onUpdateVersionInfo(UpdateVersionEvent event);
}
