# JSON to Brigadier - Minecraft

This library converts JSON files into Minecraft Commands

See [json-to-brigadier](https://github.com/oroarmor/json-to-brigadier) for the API specifications.

## Command types

### No argument types

These commands are all specified with

```json
"argument":{
  "type" : "<TYPE_HERE>"
}
```

```
"game_profile"
"block_pos"
"column_pos"
"block_state"
"block_predicate"
"item_stack"
"item_predicate"
"color"
"component"
"message"
"nbt_compound_tag"
"nbt_path"
"nbt_tag"
"objective"
"objective_criteria"
"operation"
"particle"
"angle"
"rotation"
"scoreboard_slot"
"swizzle"
"team"
"item_slot"
"resource_location"
"mob_effect"
"function"
"entity_anchor"
"int_range"
"float_range"
"item_enchantment"
"entity_summon"
"dimension"
"time"
"uuid"
```

`minecraft:vec3` and `minecraft:vec2`:

```json
"argument":{
  "type" : "<TYPE_HERE>",
  "centered": <true|false>
}
```

`minecraft:score_holder` and `minecraft:entity`:

```json
"argument":{
  "type" : "<TYPE_HERE>",
  "multiple": <true|false>
}
```