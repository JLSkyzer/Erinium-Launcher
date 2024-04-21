package fr.eriniumgroup.eriniumlauncher.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.eriniumgroup.eriniumlauncher.Launcher;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;

public class launcherSettings {
    private static File file = new File(String.valueOf(Launcher.getPath()), File.separator + "launcher_settings.json");
    private static JSONObject jsonObject = new JSONObject();

    private static void checkFileIsCreated() throws IOException {
        if (file.exists()){
            file.createNewFile();
        }
    }

    public static Object getSettings(String name){
        {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                StringBuilder jsonstringbuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    jsonstringbuilder.append(line);
                }
                bufferedReader.close();
                jsonObject = new Gson().fromJson(jsonstringbuilder.toString(), (Type) JsonObject.class);

                // jsonObject.append("Ecris", jsonObject);
                return jsonObject.get(name);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Object writevalue(String name, String value){
        {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                StringBuilder jsonstringbuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    jsonstringbuilder.append(line);
                }
                bufferedReader.close();
                jsonObject = new Gson().fromJson(jsonstringbuilder.toString(), (Type) JsonObject.class);

                // jsonObject.append("Ecris", jsonObject);
                jsonObject.append(name, value);
                {
                    com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
                    try {
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(mainGSONBuilderVariable.toJson(jsonObject));
                        fileWriter.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
