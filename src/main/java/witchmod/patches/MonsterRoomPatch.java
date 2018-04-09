package witchmod.patches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import witchmod.powers.AthamePower;


@SpirePatch(cls="com.megacrit.cardcrawl.rooms.MonsterRoom", method="getCardRarity")
public class MonsterRoomPatch {
    private static final Logger logger = LogManager.getLogger(MonsterRoomPatch.class);
	@SpireInsertPatch(rloc=5, localvars={"rareRate"})
	public static void Insert(MonsterRoom __obj_instance, int roll, int rareRate) {
		logger.info("Calling insert patch roll="+roll+" vs rare rate="+rareRate+" "+AbstractDungeon.player.hasPower(AthamePower.POWER_ID));
		if (AbstractDungeon.player.hasPower(AthamePower.POWER_ID) && AbstractDungeon.player.getPower(AthamePower.POWER_ID).amount > 0){
			rareRate += AbstractDungeon.player.getPower(AthamePower.POWER_ID).amount;
		}
		logger.info("Final rate = "+rareRate);
	}
}
