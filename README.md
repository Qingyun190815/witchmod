本仓库为女巫模组的汉化版本，原模组仓库地址：https://github.com/gygrazok/witchmod

# 女巫（The Witch）
**杀戮尖塔（Slay The Spire）** 的全新角色模组

女巫模组为游戏增添了超过 75 张新卡牌和 4 个全新遗物，围绕 **诅咒（Curses）** 展开独特的玩法体验。她的起始遗物 **黑猫（Black Cat）** 每场战斗开始时会在抽牌堆中加入一张临时随机诅咒牌，但每当你抽到诅咒牌时可获得 1 点额外能量。这一机制打破了诅咒牌的传统负面印象，令其在女巫的卡组中可能成为战略优势。

与铁卫（Ironclad）或寂静猎手（Silent）相比，女巫在游戏初期可能略显脆弱，玩法策略需要一定的探索和练习。希望玩家们能够分享宝贵的意见和建议，以帮助进一步平衡和优化她的游戏体验。

卡牌详细列表请参见 [此处](https://docs.google.com/spreadsheets/d/19tAd2g6CMNSAXdArFp2ZNpAosv3Rltb3qv1Cnc40RSk/edit?usp=sharing)。

## 核心机制 ##
* **循环（Recurrent）**：打出后不会进入弃牌堆，而是直接洗回抽牌堆，增强卡组的循环效率。
* **净化（Cleanse）**：部分特殊诅咒牌可在战斗中完成特定目标后被“净化”，解锁其隐藏效果，但该效果仅限于当前战斗。

## 新增状态效果 ##
* **腐烂（Rot）**：类似中毒，每回合对敌方造成伤害，且与中毒不同，腐烂会在每回合自动 **增加** 1 点。
* **衰弱（Decrepit）**：使受影响生物承受额外伤害，额外伤害数值取决于衰弱层数，每回合减少 1 层。

# 安装指南
本模组基于 [FruityMod-StS](https://github.com/gskleres/FruityMod-StS) 的安装指南整理，简明易懂，便于快速上手。

## 前置要求 ##
* Java 8（JRE）— 仅支持 Java 8，Java 9 兼容性问题尚在处理。
* BaseMod v3.3.0+（[下载地址](https://github.com/daviscook477/BaseMod/releases)）
* ModTheSpire v3.2.0+（[下载地址](https://github.com/kiooeht/ModTheSpire/releases)）

## 安装步骤 ##
1. 如果已安装 `ModTheSpire`，可跳至步骤 5。
2. 从 [此处](https://github.com/kiooeht/ModTheSpire/releases) 下载 `ModTheSpire.jar`
3. 将 `ModTheSpire.jar` 移动至 **杀戮尖塔** 目录，路径示例：
   `C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire`
4. 在游戏目录中创建 `mods` 文件夹：
   `C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\mods`
5. 从 [此处](https://github.com/daviscook477/BaseMod/releases) 下载 `BaseMod.jar`
6. 将 `BaseMod.jar` 放入 `mods` 文件夹
7. 从 [此处](https://github.com/gygrazok/witchmod/releases) 下载 `WitchMod.jar`
8. 将 `WitchMod.jar` 同样放入 `mods` 文件夹
9. 双击 `ModTheSpire.jar` 启动游戏
10. 在模组选择界面勾选 `BaseMod` 和 `WitchMod`，点击 **Play** 开始游戏

# 鸣谢 #
* 感谢 [**杀戮尖塔** 官方团队](https://www.megacrit.com/)，为我们提供了出色的游戏和模组支持。
* 感谢 [BaseMod](https://github.com/daviscook477) 的开发者 test447 及所有贡献者。
* 感谢 [ModTheSpire](https://github.com/kiooeht) 的开发者 kiooeht 及贡献者。
* 感谢所有提供 Bug 反馈、建议与平衡调整意见的玩家社区成员！

