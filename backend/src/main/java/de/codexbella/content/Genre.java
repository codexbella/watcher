package de.codexbella.content;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Genre {
   @SerializedName("id")
   private int apiId;

   private String name;
}
