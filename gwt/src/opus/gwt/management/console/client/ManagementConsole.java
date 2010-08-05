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

package opus.gwt.management.console.client;

import opus.gwt.management.console.client.dashboard.ProjectDashboard;
import opus.gwt.management.console.client.deployer.applicationDeployer;
import opus.gwt.management.console.client.resources.ManagementConsoleCss.ManagementConsoleStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;


public class ManagementConsole extends Composite {

	private static ManagementConsoleUiBinder uiBinder = GWT
			.create(ManagementConsoleUiBinder.class);

	interface ManagementConsoleUiBinder extends
			UiBinder<Widget, ManagementConsole> {
	}

	private final String logoutURL = "/accounts/logout/";
	private final String checkLoginURL = "/json/username/?a&callback=";
	private final String projectsURL = "/json/?a&callback=";
	
	private applicationDeployer appDeployer;
	private ProjectDashboard projectDashboard;
	private Login loginPanel;
	private IconPanel iconPanel;
	private ServerCommunicator ServerComm;
	private ManagementConsole managementCon;
	private JSVariableHandler JSVarHandler;
	private PopupPanel pp;
	private int projectCount;
	
	
	@UiField Label titleBarLabel;
	@UiField FlowPanel navigationMenuPanel;
	@UiField DeckPanel mainDeckPanel;
	@UiField ManagementConsoleStyle style;
	@UiField Button deployNewButton;
	@UiField Button dashboardsButton;
	@UiField FlowPanel topMenuFlowPanel;
	@UiField Button loggedInUserButton;
	@UiField Button authenticationButton;
	
	public ManagementConsole() {
		initWidget(uiBinder.createAndBindUi(this));
		loginPanel = new Login(mainDeckPanel, this);
		iconPanel = new IconPanel(this);
		JSVarHandler = new JSVariableHandler();
		pp = new PopupPanel();
		pp.setAutoHideEnabled(true);
		ServerComm = new ServerCommunicator();
		managementCon = this;
		checkLogin();
		pp.addCloseHandler(new CloseHandler<PopupPanel>(){
			public void onClose(CloseEvent<PopupPanel> event){
				dashboardsButton.setStyleName(style.topDashboardButton());
			}
		});

	}
	
	private void checkLogin(){
		final String url = URL.encode(JSVarHandler.getDeployerBaseURL() + checkLoginURL);
		ServerComm.getJson(url, ServerComm, "handleUserInformation", this);
	}
	
	public void loginComplete(){
		checkLogin();
	}
	
	private void createDashboardsPopup(){
		final String url = URL.encode(JSVarHandler.getDeployerBaseURL() + projectsURL);
		ServerComm.getJson(url, ServerComm, "handleProjectNames", this);	
	}
	
	public void onDeployNewProject(String projectName){
		createDashboardsPopup();
		mainDeckPanel.clear();
		navigationMenuPanel.clear();
		titleBarLabel.setText("");
    	projectDashboard = new ProjectDashboard(titleBarLabel, navigationMenuPanel, mainDeckPanel, projectName, managementCon);
	}
	
	public void onProjectDelete(){
		createDashboardsPopup();
		mainDeckPanel.clear();
		navigationMenuPanel.clear();
		titleBarLabel.setText("");
	}
	
	public ServerCommunicator getServerCommunicator(){
		return ServerComm;
	}
	
	@UiHandler("deployNewButton")
	void handleDeployNewProjectClick(ClickEvent event){
		mainDeckPanel.clear();
		navigationMenuPanel.clear();
		appDeployer = new applicationDeployer(titleBarLabel, navigationMenuPanel, mainDeckPanel, managementCon);
	}
	
	@UiHandler("dashboardsButton")
	void handleDashboardsButtonMouseOver(MouseOverEvent event){
		if(projectCount > 0) {
			dashboardsButton.setStyleName(style.topDashboardButtonActive());
			int left = dashboardsButton.getAbsoluteLeft();
			int top = dashboardsButton.getAbsoluteTop() + dashboardsButton.getOffsetHeight();
			pp.setPopupPosition(left, top);
			int width = dashboardsButton.getOffsetWidth();
			pp.setWidth(Integer.toString(width) + "px");
			pp.show();
		}
	}
	
	@UiHandler("dashboardsButton")
	void handleDashboardsButtonClick(ClickEvent event){
		mainDeckPanel.clear();
		navigationMenuPanel.clear();
		titleBarLabel.setText("");
		mainDeckPanel.add(iconPanel);
		mainDeckPanel.showWidget(0);
		if(pp.isShowing()){
			pp.hide();
		} else {
			dashboardsButton.setStyleName(style.topDashboardButtonActive());
			int left = dashboardsButton.getAbsoluteLeft();
			int top = dashboardsButton.getAbsoluteTop() + dashboardsButton.getOffsetHeight();
			pp.setPopupPosition(left, top);
			int width = dashboardsButton.getOffsetWidth();
			pp.setWidth(Integer.toString(width) + "px");
			pp.show();
		}
		pp.hide();
	}
	
	@UiHandler("authenticationButton")
	void handleAuthenticationButton(ClickEvent event){
		if(authenticationButton.getText().equals("Logout")){
			FormPanel logoutForm = new FormPanel();
			logoutForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			      public void onSubmitComplete(SubmitCompleteEvent event) {
			        checkLogin();
			      }
			 });
			logoutForm.setMethod(FormPanel.METHOD_GET);
			logoutForm.setAction(JSVarHandler.getDeployerBaseURL() + logoutURL);
			mainDeckPanel.add(logoutForm);
			logoutForm.submit();
		} else {
			showLoginPanel();
		}
	}
	
	public void handleProjectNames(JsArray<ProjectNames> ProjectNames){
		pp.clear();
		projectCount = ProjectNames.length();
		iconPanel.projectIconsFlowPanel.clear();
		if(projectCount != 0){
			FlowPanel FP = new FlowPanel();
			for(int i = 0; i < ProjectNames.length(); i++){
				final Label testLabel = new Label(ProjectNames.get(i).getName());
				testLabel.setStyleName(style.popupLabel());
				testLabel.addMouseOverHandler(new MouseOverHandler(){
					public void onMouseOver(MouseOverEvent event){
						testLabel.setStyleName(style.popupLabelActive());
					}
				});
				testLabel.addMouseOutHandler(new MouseOutHandler(){
					public void onMouseOut(MouseOutEvent event){
						testLabel.setStyleName(style.popupLabel());
					}
				});
				testLabel.addClickHandler(new ClickHandler() {
			        public void onClick(ClickEvent event) {
			        	mainDeckPanel.clear();
			    		navigationMenuPanel.clear();
			        	projectDashboard = new ProjectDashboard(titleBarLabel, navigationMenuPanel, mainDeckPanel, testLabel.getText(), managementCon); 
			        	if(pp.isShowing()){
			    			dashboardsButton.setStyleName(style.topDashboardButton());
			    			pp.hide();
			    		}   	
			        }
			     });
				FP.add(testLabel);
				//Add to the icon panel
				iconPanel.addProjectIcon(ProjectNames.get(i).getName());
			}
			pp.add(FP);
			mainDeckPanel.add(iconPanel);
			mainDeckPanel.showWidget(0);
		} else {
			
			deployNewButton.click();
		}
		pp.setStyleName(style.dashboardsPopup());
	}
	
	public void handleUserInformation(UserInformation userInfo){
		if(userInfo.isAuthenticated()){
			loggedInUserButton.setText("Logged in as: " + userInfo.getUsername());
			loggedInUserButton.setVisible(true);
			deployNewButton.setVisible(true);
			dashboardsButton.setVisible(true);
			authenticationButton.setVisible(true);
			mainDeckPanel.clear();
			navigationMenuPanel.clear();
			createDashboardsPopup();
			String token = JSVarHandler.getProjectToken();
			if (token != null) {
				deployNewButton.click();
			}
		} else {
			showLoginPanel();
		}
	}
	
	private void showLoginPanel(){
		mainDeckPanel.clear();
		titleBarLabel.setText("");
		navigationMenuPanel.clear();
		loginPanel = new Login(mainDeckPanel, this);
		mainDeckPanel.add(loginPanel);
		mainDeckPanel.showWidget(0);
		loggedInUserButton.setVisible(false);
		authenticationButton.setVisible(false);
		dashboardsButton.setVisible(false);
		deployNewButton.setVisible(false);
	}
	
	public final native JsArray<ProjectNames> asArrayOfProjectNames(JavaScriptObject jso) /*-{
    	return jso;
  	}-*/;
	
	public final native UserInformation asJSOUserInformation(JavaScriptObject jso) /*-{
		return jso;
	}-*/;
}