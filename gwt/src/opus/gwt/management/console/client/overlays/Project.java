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

package opus.gwt.management.console.client.overlays;


import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public class Project extends JavaScriptObject {

	protected Project(){}
	
	public final native String getName() /*-{ return this.name; }-*/;
	public final native JsArrayString getApps() /*-{ return this.apps; }-*/;
	public final native JsArrayString getURLS() /*-{ return this.urls; }-*/;
	public final native boolean isActive() /*-{ return this.active; }-*/;
	public final native String getDBName()  /*-{ return this.dbname; }-*/;
	public final native String getDBEngine()  /*-{ return this.dbengine; }-*/;
	public final native ProjectSettingsData getAppSettings() /*-{ return this.appsettings }-*/;
}
