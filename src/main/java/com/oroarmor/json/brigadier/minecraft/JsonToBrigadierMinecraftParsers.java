/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.oroarmor.json.brigadier.minecraft;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.oroarmor.json.brigadier.BrigadierArgumentParsers;
import com.oroarmor.json.brigadier.JsonArgumentParsers;
import com.oroarmor.json.brigadier.minecraft.mixin.*;
import net.minecraft.SharedConstants;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ScoreHolderArgumentType;
import net.minecraft.command.argument.Vec2ArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class JsonToBrigadierMinecraftParsers {
    public static final List<String> NO_PARAMETERS = new ArrayList<>();

    static {
        NO_PARAMETERS.add("game_profile");
        NO_PARAMETERS.add("block_pos");
        NO_PARAMETERS.add("column_pos");
        NO_PARAMETERS.add("block_state");
        NO_PARAMETERS.add("block_predicate");
        NO_PARAMETERS.add("item_stack");
        NO_PARAMETERS.add("item_predicate");
        NO_PARAMETERS.add("color");
        NO_PARAMETERS.add("component");
        NO_PARAMETERS.add("message");
        NO_PARAMETERS.add("nbt_compound_tag");
        NO_PARAMETERS.add("nbt_path");
        NO_PARAMETERS.add("nbt_tag");
        NO_PARAMETERS.add("objective");
        NO_PARAMETERS.add("objective_criteria");
        NO_PARAMETERS.add("operation");
        NO_PARAMETERS.add("particle");
        NO_PARAMETERS.add("angle");
        NO_PARAMETERS.add("rotation");
        NO_PARAMETERS.add("scoreboard_slot");
        NO_PARAMETERS.add("swizzle");
        NO_PARAMETERS.add("team");
        NO_PARAMETERS.add("item_slot");
        NO_PARAMETERS.add("resource_location");
        NO_PARAMETERS.add("mob_effect");
        NO_PARAMETERS.add("function");
        NO_PARAMETERS.add("entity_anchor");
        NO_PARAMETERS.add("int_range");
        NO_PARAMETERS.add("float_range");
        NO_PARAMETERS.add("item_enchantment");
        NO_PARAMETERS.add("entity_summon");
        NO_PARAMETERS.add("dimension");
        NO_PARAMETERS.add("time");
        NO_PARAMETERS.add("uuid");
        if (SharedConstants.isDevelopment) {
            NO_PARAMETERS.add("test_argument");
            NO_PARAMETERS.add("test_class");
        }

        for (String type : NO_PARAMETERS) {
            Identifier id = new Identifier(type);

            JsonArgumentParsers.register(id.toString(), new JsonArgumentParsers.ArgumentParser() {
                @Override
                @SuppressWarnings("unchecked")
                public <Type, Self extends ArgumentBuilder<Type, Self>> ArgumentBuilder<Type, Self> parse(JsonObject commandObject) {
                    try {
                        Class<ArgumentType<Self>> aClass = (Class<ArgumentType<Self>>) ArgumentTypesAccessor.callById(id).argClass;
                        return (ArgumentBuilder<Type, Self>) RequiredArgumentBuilder.<Type, Self>argument(commandObject.get("name").getAsString(), aClass.newInstance());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });


            BrigadierArgumentParsers.register((Class<? extends ArgumentType<?>>) ArgumentTypesAccessor.callById(id).argClass, new BrigadierArgumentParsers.CommandNodeParser() {
                @Override
                public <T> void parse(JsonObject argument, ArgumentType<T> type) {
                    argument.addProperty("type", id.toString());
                }
            });
        }

        JsonArgumentParsers.register(new Identifier("entity").toString(), JsonToBrigadierMinecraftParsers::parseEntity);
        JsonArgumentParsers.register(new Identifier("score_holder").toString(), JsonToBrigadierMinecraftParsers::parseScoreHolder);
        JsonArgumentParsers.register(new Identifier("vec3").toString(), JsonToBrigadierMinecraftParsers::parseVec3);
        JsonArgumentParsers.register(new Identifier("vec2").toString(), JsonToBrigadierMinecraftParsers::parseVec2);

        BrigadierArgumentParsers.register(EntityArgumentType.class, JsonToBrigadierMinecraftParsers::parseEntityArg);
        BrigadierArgumentParsers.register(ScoreHolderArgumentType.class, JsonToBrigadierMinecraftParsers::parseScoreHolderArg);
        BrigadierArgumentParsers.register(Vec3ArgumentType.class, JsonToBrigadierMinecraftParsers::parseVec3Arg);
        BrigadierArgumentParsers.register(Vec2ArgumentType.class, JsonToBrigadierMinecraftParsers::parseVec2Arg);
    }

    @SuppressWarnings("unchecked")
    private static <Type, Self extends ArgumentBuilder<Type, Self>> ArgumentBuilder<Type, Self> parseEntity(JsonObject jsonCommand) {
        return (ArgumentBuilder<Type, Self>) RequiredArgumentBuilder.argument(jsonCommand.get("name").getAsString(), jsonCommand.has("multiple") && jsonCommand.get("multiple").getAsBoolean() ? EntityArgumentType.entities() : EntityArgumentType.entity());
    }

    @SuppressWarnings("unchecked")
    private static <Type, Self extends ArgumentBuilder<Type, Self>> ArgumentBuilder<Type, Self> parseScoreHolder(JsonObject jsonCommand) {
        return (ArgumentBuilder<Type, Self>) RequiredArgumentBuilder.argument(jsonCommand.get("name").getAsString(), jsonCommand.has("multiple") && jsonCommand.get("multiple").getAsBoolean() ? ScoreHolderArgumentType.scoreHolder() : ScoreHolderArgumentType.scoreHolders());
    }

    @SuppressWarnings("unchecked")
    private static <Type, Self extends ArgumentBuilder<Type, Self>> ArgumentBuilder<Type, Self> parseVec3(JsonObject jsonCommand) {
        return (ArgumentBuilder<Type, Self>) RequiredArgumentBuilder.argument(jsonCommand.get("name").getAsString(), Vec3ArgumentType.vec3(jsonCommand.has("centered")));
    }

    @SuppressWarnings("unchecked")
    private static <Type, Self extends ArgumentBuilder<Type, Self>> ArgumentBuilder<Type, Self> parseVec2(JsonObject jsonCommand) {
        return (ArgumentBuilder<Type, Self>) RequiredArgumentBuilder.argument(jsonCommand.get("name").getAsString(), new Vec2ArgumentType(jsonCommand.has("centered")));
    }

    @SuppressWarnings("unchecked")
    private static <T> void parseEntityArg(JsonObject argument, ArgumentType<T> type) {
        argument.addProperty("type", "minecraft:entity");
        argument.addProperty("multiple", ((EntityArgumentTypeAccessor) type).getSingleTarget());
    }

    @SuppressWarnings("unchecked")
    private static <T> void parseScoreHolderArg(JsonObject argument, ArgumentType<T> type) {
        argument.addProperty("type", "minecraft:score_holder");
        argument.addProperty("multiple", ((ScoreHolderArgumentTypeAccessor) type).getMultiple());
    }

    @SuppressWarnings("unchecked")
    private static <T> void parseVec3Arg(JsonObject argument, ArgumentType<T> type) {
        argument.addProperty("type", "minecraft:vec3");
        argument.addProperty("centered", ((Vec3ArgumentTypeAccessor) type).getCenterIntegers());
    }

    @SuppressWarnings("unchecked")
    private static <T> void parseVec2Arg(JsonObject argument, ArgumentType<T> type) {
        argument.addProperty("name", "minecraft:vec2");
        argument.addProperty("centered", ((Vec2ArgumentTypeAccessor) type).getCenterIntegers());
    }

    public static void initialize() {
    }
}
