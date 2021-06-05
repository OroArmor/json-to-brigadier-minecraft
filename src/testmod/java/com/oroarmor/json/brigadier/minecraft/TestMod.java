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

import com.google.gson.GsonBuilder;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.oroarmor.json.brigadier.BrigadierToJson;
import com.oroarmor.json.brigadier.JsonToBrigadier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.core.jmx.Server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

public class TestMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            try {
                dispatcher.register((LiteralArgumentBuilder<ServerCommandSource>) JsonToBrigadier.<ServerCommandSource, LiteralArgumentBuilder<ServerCommandSource>>parse(
                        Path.of((TestMod.class.getClassLoader().getResource("data/test_command.json").toURI()))
                ));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


            String json = new GsonBuilder().setPrettyPrinting().create().toJson(BrigadierToJson.parseObject(dispatcher));
            Path exportFile = FabricLoader.getInstance().getGameDir().resolve("export.json");

            try {
                Files.write(exportFile, json.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static int runTestCommand(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(new LiteralText("Successful!"), true);
        return 1;
    }
}
