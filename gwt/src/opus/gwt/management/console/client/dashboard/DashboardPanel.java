/*############################################################################
# Copyright 2010 North Carolina State University                             #
#                                                                            #
#   Licensed under the Apache License, Version 2.0 (the "License");          #
#   you may not use this file except in compliance with the License.         #
#   You may obtain a copy of the License at                                  #
#                                                                            #
#       http://www.apache.org/licenses/LICENSE-2.0                           #
#                                                                            #
#   Unless required by applicable law or agreed to in writing, software      #
#   distributed under the License is distributed on an "AS IS" BASIS,        #
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. #
#   See the License for the specific language governing permissions and      #
#   limitations under the License.                                           #
############################################################################*/

package opus.gwt.management.console.client.dashboard;

import opus.gwt.management.console.client.event.AsyncRequestEvent;
import opus.gwt.management.console.client.event.UpdateProjectsEvent;
import opus.gwt.management.console.client.event.UpdateProjectsEventHandler;
import opus.gwt.management.console.client.overlays.Project;
import opus.gwt.management.console.client.overlays.ProjectSettingsData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DashboardPanel extends Composite {

	private static DashboardUiBinder uiBinder = GWT.create(DashboardUiBinder.class);
	interface DashboardUiBinder extends UiBinder<Widget, DashboardPanel> {}
	
	private JavaScriptObject appSettings;
	private boolean active;
	private EventBus eventBus;

	@UiField Label dbnameLabel;
	@UiField Label dbengineLabel;
	@UiField Label activeLabel;
	@UiField FlowPanel urlsFlowPanel;
	
	public DashboardPanel(EventBus eventBus) {
		initWidget(uiBinder.createAndBindUi(this));
		this.eventBus = eventBus;
		registerEvents();
	}
	
	private void registerEvents(){
		eventBus.addHandler(UpdateProjectsEvent.TYPE, 
			new UpdateProjectsEventHandler(){
				public void onUpdateProjects(UpdateProjectsEvent event){
					//handleProjectInformation(event.getProjects());
				}
		});
	}
	
	private void getProjectInfo(String projectName){
		eventBus.fireEvent(new AsyncRequestEvent("handleProjectInformation", projectName));
	}
	
	public void handleProjectInformation(Project projInfo){
		dbnameLabel.setText(projInfo.getDBName());
		dbengineLabel.setText(projInfo.getDBEngine());
		String httpUrl = projInfo.getURLS().get(0);
		String httpsUrl = projInfo.getURLS().get(1);
		
		if(projInfo.isActive()){
			activeLabel.setText("Yes");
			active = true;
		} else {
			activeLabel.setText("No");
			active = false;
		}
		for(int i =0; i < projInfo.getApps().length(); i++){
			int index = projInfo.getApps().get(i).indexOf(".");
			urlsFlowPanel.add(new HTMLPanel("<label>" + projInfo.getApps().get(i) + ": </label><br>" + "<a href='" + httpUrl + projInfo.getApps().get(i) + "/'>" + httpUrl + projInfo.getApps().get(i) + "/</a>" ));
			urlsFlowPanel.add(new HTMLPanel("<a href='" + httpsUrl + projInfo.getApps().get(i) + "/'>" + httpsUrl + projInfo.getApps().get(i) + "/</a><br><br>" ));	
		}
		try {
			ProjectSettingsData settings = projInfo.getAppSettings();
			String a = settings.getApplicationSettings();
			//Get list of apps
			String[] apps = a.split(";;;\\s*");
			//Create panel to display options for all apps
			//ProjectSettingsPanel options = projectManagerController.getOptionsPanel();
			//options.importProjectSettings(settings, apps);
			//projectManagerController.getOptionsPanel().setHasSettings(true);

			//projectManagerController.getOptionsPanel().setActive(this.active);
		} catch (Exception e) {
			//projectManagerController.getOptionsPanel().setHasSettings(false);
			//projectManagerController.getOptionsPanel().setActive(this.active);
		}
	}
	
	public boolean isActive(){
		return active;
	}
}
