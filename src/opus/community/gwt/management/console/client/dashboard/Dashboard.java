package opus.community.gwt.management.console.client.dashboard;

import opus.community.gwt.management.console.client.JSVariableHandler;
import opus.community.gwt.management.console.client.ServerCommunicator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Dashboard extends Composite {

	private static DashboardUiBinder uiBinder = GWT
			.create(DashboardUiBinder.class);

	interface DashboardUiBinder extends UiBinder<Widget, Dashboard> {
	}

	private ServerCommunicator serverComm;
	private JSVariableHandler JSVarHandler;

	@UiField FlowPanel applicationsFlowPanel;
	@UiField Label dbnameLabel;
	@UiField Label dbengineLabel;
	@UiField Label activeLabel;
	@UiField FlowPanel urlsFlowPanel;
	
	public Dashboard(String projectName, ServerCommunicator serverComm) {
		initWidget(uiBinder.createAndBindUi(this));
		this.serverComm = serverComm;
		JSVarHandler = new JSVariableHandler();
		getProjectInfo(projectName);
	}
	
	private void getProjectInfo(String projectName){
		final String url = URL.encode(JSVarHandler.getDeployerBaseURL() + "json/" + projectName + "/?a&callback=");
		serverComm.getJson(url, serverComm, 5, this);
	}
	
	public void handleProjectInformation(ProjectInformation projInfo){
		dbnameLabel.setText(projInfo.getDBName());
		dbengineLabel.setText(projInfo.getDBEngine());
		if(projInfo.getActive()){
			activeLabel.setText("Yes");
		} else {
			activeLabel.setText("No");
		}
		for(int i =0; i < projInfo.getApps().length(); i++){
			int index = projInfo.getApps().get(i).indexOf(".");
			applicationsFlowPanel.add(new Label(projInfo.getApps().get(i).substring(index+1)));	
		}
		for(int i =0; i < projInfo.getURLS().length(); i++){
			urlsFlowPanel.add(new HTML("<a href='" + projInfo.getURLS().get(i) + "'>" + projInfo.getURLS().get(i) + "</a>"));	
		}
	}
	
	public final native ProjectInformation asJSOProjectInformation(JavaScriptObject jso) /*-{
		return jso;
	}-*/;
	
}
