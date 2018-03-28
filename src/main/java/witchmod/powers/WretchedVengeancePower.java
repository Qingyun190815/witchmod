package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import witchmod.WitchMod;

public class WretchedVengeancePower extends AbstractPower {
    public static final String POWER_ID = "WretchedVengeancePower";
    public static final String NAME = "Wretched Vengeance";
    public static final String[] DESCRIPTIONS = new String[]{ "When you are damaged apply #b"," Decrepit to the attacker."};
    public static final String IMG = "powers/athamesoffering.png";
    public WretchedVengeancePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.img = new Texture(WitchMod.getResourcePath(IMG));
        this.type = PowerType.BUFF;
        
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner) {
            this.flash();
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(info.owner, owner, new DecrepitPower(info.owner, amount), amount, true));
        }
        return damageAmount;
    }
    
    @Override
    public void updateDescription() {
    	description = DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];
    }
    
  
    
}

