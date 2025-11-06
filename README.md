# 🎁 FortuneBox - Minecraft 抽奖插件

一个轻量、易扩展的抽奖箱插件，灵感来自 **CrazyCrates**，使用 **Kotlin + TabooLib** 构建。  
支持自定义奖池、动画、音效与多种触发方式，让服务器活动更有趣！

---

## ✨ 功能特点
- 🧱 在世界中放置“抽奖箱”实体
- 🗂️ 奖励内容完全可配置（物品、指令、权限等）
- 💾 使用 YAML 文件保存数据
- 🔄 兼容 Spigot 1.12.2-1.21

---

## 🧩 插件依赖
- [TabooLib 6.2](https://github.com/TabooLib/TabooLib)

---

## 📥 安装方式
1. 下载最新版本的 `FortuneBox.jar`
2. 放入服务器的 `/plugins/` 文件夹
3. 启动服务器自动生成配置文件
4. 编辑 `config.yml` 和 `boxes/` 目录下的奖池配置
5. 重启或执行 `/fortunebox reload` 生效

---

## 💻 常用命令
| 命令                                       | 说明       |
|------------------------------------------|----------|
| `/fortunebox`                            | 主命令，显示帮助 |
| `/fortunebox set <BoxName>`                | 设置指定抽奖箱  |
| `/fortunebox give <Player> <BoxName> [数量]` | 给予抽奖钥匙   |
| `/fortunebox reload`                       | 重载配置文件   |

---

## 🔐 权限节点
| 权限                            | 默认 | 说明          |
|-------------------------------|------|-------------|
| `fortunebox.boardcast.common` | true | 允许玩家接收抽奖广播  |
| `fortunebox.command.set`      | op | 允许管理员设置抽奖箱  |
| `fortunebox.command.give`     | op | 允许管理员给予抽奖钥匙 |
| `fortunebox.command.get`      | op | 允许管理员获取抽奖钥匙 |
| `fortunebox.command.reload`   | op | 允许使用重载命令    |

---

## ⚙️ 配置示例

box.yml
```yaml
Box:
  BoxType: CSGO
  Name: "§a普通抽奖箱"
  BoardCast:
    Enable: true
    OpeningBoardCast: "%prefix% §e%player% §f正在打开 §a普通抽奖箱"
    CompletedBoardCast: "%prefix% §e%player% §f获得了 %prize%"
  OpeningCommand:
    Enable: false
    Commands:
      - "say %player% 打开了 §a普通抽奖箱"
  Sound:
    Cycle:
      Enable: true
      Value: "block.note_block.xylophone"
      Volume: 1.0
      Pitch: 1.0
    Stop:
      Enable: true
      Value: "entity.player.levelup"
      Volume: 1.0
      Pitch: 1.0
  PrizeMessage:
    - "你从 §a%box% §f获得了 %prize%"
  PrizeCommands: []
  Preview:
    Enable: true
    Name: "§a普通抽奖箱§f预览"
  Animation: "§d抽奖中..."
  Hologram:
    Enable: true
    Height: 1.5
    Range: 12
    UpdateInterval: -1
    Content:
      - "§a普通抽奖箱"
      - "每次抽奖需要1个钥匙"
  RequireKeys: 1
  Key:
    Name: "§a普通抽奖箱钥匙"
    Lore:
      - "用于开启 §a普通抽奖箱"
    Item: "tripwire_hook"
    CustomModelData: -1
    Model:
      Namespace: ""
      Key: ""
    Glowing: true
  Prize:
    - {Key: "钻石",Weight: 1}
    - {Key: "铁",Weight: 4}
```

prizes.yml
```yaml
钻石:
  Name: "§b钻石"
  DisplayName: "§d钻石"
  DisplayItem: diamond_block
  DisplayAmount: 2
  DisplayLore:
    - "钻石"
  Glowing: true
  DisplayCustomModelData: -1
  DisplayModel:
    Namespace: ""
    Key: ""
  DisplayRate: "§c概率: 25%"
  Items:
    - {Item: diamond, Amount: 1, Name: "钻石", Lore: ["普通的钻石"],CustomModelData: -1,Model:{Namespace: "",Key: ""}}
    - {Item: iron_ingot, Amount: 1, Name: "铁233", Lore: ["§b铁"]}
  Commands:
    - xp add %player% 100
铁:
  Name: "§8铁"
  DisplayItem: iron_ingot
  Items:
    - {Item: iron_ingot, Amount: 2}
mm物品1:
  Name: "§b物品1"
  DisplayItem: "Mythic:物品1"
  Items:
    - {Item: "Mythic:物品1", Amount: 1}

