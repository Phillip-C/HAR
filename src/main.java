import java.io.FileReader;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

public class main implements BurpExtension{
	public MontoyaApi api;
	@Override
	public void initialize(MontoyaApi api) {
		this.api = api;
		api.extension().setName("HAR Importer");	
		api.logging().logToOutput("[+] HAR Importer Loaded");
		GUI UI = new GUI(api);
		api.userInterface().registerSuiteTab("HAR Importer", UI);
	}
	
	public static void main(String[] args) {
		
	}
}
