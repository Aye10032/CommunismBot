package com.aye10032.foundation.utils.fangzhoudiaoluo;

import org.apache.commons.io.IOUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Dazo66
 */
public class Module {

    String rawModule;
    List<String> rawModules;

    public static Module module;
    public static ModuleMaterial moduleMaterial;
    public static ModuleStage moduleStage;
    public static ModuleDrop moduleDrop;
    public static String lastUpdate = "";

    public Module(String rawModule) {
        this.rawModule = rawModule;
        rawModules = getModules(rawModule);
    }

    public static void update(String dir) throws IOException {
        //HttpClient client1 = HttpClientBuilder.create().setDefaultHeaders(Arrays.asList(FangZhouDiaoluoFunc.getHeaders())).build();

        FileReader reader = new FileReader(dir + "/fangzhoudiaoluo/module.txt");
        module = new Module(IOUtils.toString(reader));
        reader.close();

        reader = new FileReader(dir + "/fangzhoudiaoluo/material_module.txt");
        moduleMaterial = new ModuleMaterial(IOUtils.toString(reader));
        reader.close();

        reader = new FileReader(dir + "/fangzhoudiaoluo/stages_module.txt");
        moduleStage = new ModuleStage(IOUtils.toString(reader));
        reader.close();

        reader = new FileReader(dir + "/fangzhoudiaoluo/extera_drop_module.txt");
        moduleDrop = new ModuleDrop(IOUtils.toString(reader));
        reader.close();
    }

    public static String getVer(String rawModule) {
        return rawModule.substring(rawModule.indexOf("{"), rawModule.indexOf("}") + 1);
    }

    public static List<String> getModules(String rawModule) {
        List<String> modules = new ArrayList<>();
        while (rawModule.contains("]")) {
            int index = rawModule.indexOf("]");
            if (index > -1) {
                modules.add(rawModule.substring(0, index + 1).replace("[", "").replace("]", ""));
                rawModule = rawModule.substring(index + 1);
            } else {
                modules.add(rawModule);
            }
        }
        return modules;
    }

    public static List<String> getVers(String rawVers) {
        List<String> vers = new ArrayList<>();
        while (rawVers.contains("}")) {
            int index = rawVers.indexOf("}");
            if (index > -1) {
                vers.add(rawVers.substring(0, index + 1).replace("{", "").replace("}", ""));
                rawVers = rawVers.substring(index + 1);
            } else {
                vers.add(rawVers);
            }
        }
        return vers;
    }

    public static int getSecIndexof(String s, String cher) {
        String[] arr = s.split(cher);
        if (arr.length > 1) {
            return (arr[0] + arr[1] + cher).length();
        } else {
            return -1;
        }
    }

    public Map<String, Function<DiaoluoType.Material, String>> verFuncMaterial = new HashMap<>();

    {
        verFuncMaterial.put("{name}", material -> material.name);
        verFuncMaterial.put("{material_module}", material -> moduleMaterial.getString(material));
        verFuncMaterial.put("{last_update}", material -> lastUpdate);
    }

    public String getString(DiaoluoType.Material material) {
        StringBuilder ret = new StringBuilder();
        for (String m : rawModules) {
            String ver = getVer(m);
            if (verFuncMaterial.get(ver) != null) {
                String line = verFuncMaterial.get(ver).apply(material);
                String s2 = m.substring(0, m.indexOf("{"));
                if (m.contains("\n") && m.indexOf("\n") < m.indexOf("{") - 1) {
                    s2 = s2.substring(s2.lastIndexOf("\n"));
                }
                int count = s2.length();
                line = line.replace("\n", "\n" + getSpaces(count - 1));
                if (!"".equals(line.trim())) {
                    ret.append(m.replace(ver, line));
                }
            }
        }
        return ret.toString();
    }

    public String getSpaces(int count) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < count; i++) {
            s.append(" ");
        }
        return s.toString();
    }


    static class ModuleMaterial extends Module {

        ModuleMaterial(String rawModule) {
            super(rawModule);
        }

        {

            verFuncMaterial.put("{name}", material -> material.name);
            verFuncMaterial.put("{credit_store_value}", material -> String.valueOf(material.credit_store_value));
            verFuncMaterial.put("{green_ticket_value}", material -> String.valueOf(material.green_ticket_value));
            verFuncMaterial.put("{golden_ticket_value}", material -> String.valueOf(material.golden_ticket_value));
            verFuncMaterial.put("{lowest_ap_stages}", material -> {
                String string = "";
                int i = material.lowest_ap_stages.length;
                for (DiaoluoType.Stage stage : material.lowest_ap_stages) {
                    string += moduleStage.getString(stage);
                    i--;
                    if (i > 0) {
                        string += "\n------------\n";
                    } else {
                        string += "\n\n";
                    }
                }
                if (material.lowest_ap_stages.length > 0) {
                    return string.substring(0, string.length() - 1);
                } else {
                    return "";
                }
            });
            verFuncMaterial.put("{balanced_stages}", material -> {
                String string = "";
                int i = material.balanced_stages.length;
                for (DiaoluoType.Stage stage : material.balanced_stages) {
                    string += moduleStage.getString(stage);
                    i--;
                    if (i > 0) {
                        string += "\n------------\n";
                    } else {
                        string += "\n\n";
                    }
                }
                if (material.balanced_stages.length > 0) {
                    return string.substring(0, string.length() - 1);
                } else {
                    return "";
                }
            });
            verFuncMaterial.put("{drop_rate_first_stages}", material -> {
                String string = "";
                int i = material.drop_rate_first_stages.length;
                for (DiaoluoType.Stage stage : material.drop_rate_first_stages) {
                    string += moduleStage.getString(stage);
                    i--;
                    if (i > 0) {
                        string += "\n------------\n";
                    } else {
                        string += "\n\n";
                    }
                }
                if (material.drop_rate_first_stages.length > 0) {
                    return string.substring(0, string.length() - 1);
                } else {
                    return "";
                }
            });
        }
    }

    static class ModuleStage extends Module {

        ModuleStage(String rawModule) {
            super(rawModule);
        }

        Map<String, Function<DiaoluoType.Stage, String>> verFuncStage = new HashMap<>();

        {
            verFuncStage.put("{code}", stage -> stage.code);
            verFuncStage.put("{drop_rate}", stage -> String.valueOf(stage.drop_rate));
            verFuncStage.put("{efficiency}", stage -> String.valueOf(stage.efficiency));
            verFuncStage.put("{ap_per_item}", stage -> String.valueOf(stage.ap_per_item));
            verFuncStage.put("{extra_drop}", stage -> {
                String string = "";
                string += moduleDrop.getString(stage.extra_drop);
                return string;
            });

        }

        public String getString(DiaoluoType.Stage stage) {
            StringBuilder ret = new StringBuilder();
            for (String m : rawModules) {
                String ver = getVer(m);
                if (verFuncStage.get(ver) != null) {
                    String line = verFuncStage.get(ver).apply(stage);
                    String s2 = m.substring(0, m.indexOf("{"));
                    if (m.contains("\n") && m.indexOf("\n") < m.indexOf("{") - 1) {
                        s2 = s2.substring(s2.indexOf("\n"));
                    }
                    int count = s2.length();
                    try {
                        count = s2.getBytes(StandardCharsets.UTF_8).length;
                    } catch (Exception e) {
                        //ignore
                    }
                    line = line.replace("\n", "\n" + getSpaces(count));
                    if (!"".equals(line.trim())) {
                        ret.append(m.replace(ver, line));
                    }
                }

            }
            return ret.toString();
        }
    }

    static class ModuleDrop extends Module {

        ModuleDrop(String rawModule) {
            super(rawModule);
        }

        Map<String, Function<DiaoluoType.Drop[], String>> verFuncDrop = new HashMap<>();

        {
            verFuncDrop.put("{extra_drop_items}", drops -> {
                StringBuilder string = new StringBuilder();
                for (DiaoluoType.Drop drop : drops) {
                    string.append(drop.name);
                    string.append(" ");
                }
                return string.toString().trim();
            });


        }

        public String getString(DiaoluoType.Drop[] drops) {
            StringBuilder ret = new StringBuilder();
            for (String m : rawModules) {
                String ver = getVer(m);
                if (verFuncDrop.get(ver) != null) {
                    String line = verFuncDrop.get(ver).apply(drops);
                    String s2 = m.substring(0, m.indexOf("{"));
                    if (m.contains("\n") && m.indexOf("\n") < m.indexOf("{") - 1) {
                        s2 = s2.substring(s2.indexOf("\n"));
                    }
                    int count = s2.length();
                    try {
                        count = s2.getBytes(StandardCharsets.UTF_8).length;
                    } catch (Exception e) {
                        //ignore
                    }
                    line = line.replace("\n", "\n" + getSpaces(count));
                    if (!"".equals(line.trim())) {
                        ret.append(m.replace(ver, line));
                    }
                }
            }
            return ret.toString();
        }
    }

}


