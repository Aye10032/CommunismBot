package com.aye10032.Utils.fangzhoudiaoluo;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dazo66
 */
public class MaterialsDeserializer implements JsonDeserializer<DiaoluoType.Material[]> {
    @Override
    public DiaoluoType.Material[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray raw_materials = json.getAsJsonArray();
        List<DiaoluoType.Material> list = new ArrayList<>();
        raw_materials.forEach(jsonElement -> {
            DiaoluoType.Material material = new DiaoluoType.Material();
            material.id = jsonElement.getAsJsonObject().get("id").getAsString();
            material.tier = jsonElement.getAsJsonObject().get("tier").getAsInt();
            material.name = jsonElement.getAsJsonObject().get("name").getAsString();

            try {material.credit_store_value = jsonElement.getAsJsonObject().get("credit_store_value").getAsJsonObject().get("event").getAsString();}catch (Exception ignored) {}
            try {material.green_ticket_value = jsonElement.getAsJsonObject().get("green_ticket_value").getAsJsonObject().get("event").getAsString();}catch (Exception ignored) {}
            try {material.golden_ticket_value = jsonElement.getAsJsonObject().get("golden_ticket_value").getAsJsonObject().get("event").getAsString();}catch (Exception ignored) {}

            material.type = jsonElement.getAsJsonObject().get("type").getAsString();
            try {material.lowest_ap_stages = context.deserialize(jsonElement.getAsJsonObject().get("lowest_ap_stages").getAsJsonObject().get("event").getAsJsonArray(), DiaoluoType.Stage[].class);}catch (Exception ignored) {}
            try {material.balanced_stages = context.deserialize(jsonElement.getAsJsonObject().get("balanced_stages").getAsJsonObject().get("event").getAsJsonArray(), DiaoluoType.Stage[].class);}catch (Exception ignored) {}
            try {material.drop_rate_first_stages = context.deserialize(jsonElement.getAsJsonObject().get("drop_rate_first_stages").getAsJsonObject().get("event").getAsJsonArray(), DiaoluoType.Stage[].class);}catch (Exception ignored) {}
            list.add(material);
        });
        return list.toArray(new DiaoluoType.Material[0]);
    }
}
