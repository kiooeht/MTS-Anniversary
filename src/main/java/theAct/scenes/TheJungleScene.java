package theAct.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.vfx.scene.FireFlyEffect;
import theAct.TheActMod;

import java.util.ArrayList;
import java.util.Iterator;

public class TheJungleScene extends AbstractScene {

	private TextureAtlas.AtlasRegion bg;
	private TextureAtlas.AtlasRegion fg;
	private TextureAtlas.AtlasRegion ceil;
	private TextureAtlas.AtlasRegion fgGlow;
	private TextureAtlas.AtlasRegion floor;
	private TextureAtlas.AtlasRegion mg1;
	private Texture campfirebg;
	private Texture campfire;
	private Texture fire;
	private ArrayList<FireFlyEffect> fireflies;
	private ArrayList<Tree> trees;
	private static Texture treeTexture;
	private static Texture topBar;

	public TheJungleScene() {
		super(TheActMod.assetPath("images/theJungleScene/atlas.atlas"));
		if(treeTexture == null){
			treeTexture = ImageMaster.loadImage(TheActMod.assetPath("/images/theJungleScene/tree.png"));
			topBar = ImageMaster.loadImage(TheActMod.assetPath("/images/theJungleScene/topbar.png"));
		}

		this.fireflies = new ArrayList<>();
		this.trees = new ArrayList<>();
		this.bg = this.atlas.findRegion("mod/bg1");
		this.fg = this.atlas.findRegion("mod/fg");
		this.ceil = this.atlas.findRegion("mod/ceiling");
		this.fgGlow = this.atlas.findRegion("mod/fgGlow");
		this.floor = this.atlas.findRegion("mod/floor");
		this.mg1 = this.atlas.findRegion("mod/mg1");

		this.ambianceName = "AMBIANCE_CITY";
		this.fadeInAmbiance();
	}

	@Override
	public void update() {
		super.update();
		this.updateFireFlies();
	}

	private void updateFireFlies() {
		final Iterator<FireFlyEffect> e = this.fireflies.iterator();
		while (e.hasNext()) {
			final FireFlyEffect effect = e.next();
			effect.update();
			if (effect.isDone) {
				e.remove();
			}
		}
		if (this.fireflies.size() < 9 && !Settings.DISABLE_EFFECTS && MathUtils.randomBoolean(0.1f)) {
			this.fireflies.add(new FireFlyEffect(new Color(MathUtils.random(0.1f, 0.2f), MathUtils.random(0.6f, 0.8f), MathUtils.random(0.8f, 1.0f), 1.0f)));
		}
	}

	@Override
	public void randomizeScene() {
		trees.clear();
		for(int i = 0; i < MathUtils.random(2, 5); i++) {
			float xMin = Settings.WIDTH / 4f;
			float xMax = (xMin + Settings.WIDTH / 2f);

			trees.add(new Tree(
				MathUtils.random(xMin, xMax),
				Settings.HEIGHT / 2f));
		}
	}

	@Override
	public void nextRoom(AbstractRoom room) {
		super.nextRoom(room);
		this.fireflies.clear();
		this.randomizeScene();
		if(room instanceof MonsterRoomBoss) {
			CardCrawlGame.music.silenceBGM();
		}
		this.fadeInAmbiance();
	}

	@Override
	public void renderCombatRoomBg(SpriteBatch sb) {
		sb.setColor(Color.WHITE.cpy());
		this.renderAtlasRegionIf(sb, bg, true);
		sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);
		this.renderAtlasRegionIf(sb, this.floor, true);
		this.renderAtlasRegionIf(sb, this.ceil, true);
		this.renderAtlasRegionIf(sb, this.mg1, true);
		for(Tree t : trees) {
			sb.draw(
				treeTexture,
				t.x - treeTexture.getWidth() / 2f,
				t.y - 290,
				0,
				0,
				treeTexture.getWidth(),
				treeTexture.getHeight(),
				Settings.scale * 0.9f,
				Settings.scale * 0.9f,
				0,
				0,
				0,
				treeTexture.getWidth(),
				treeTexture.getHeight(),
				t.flip,
				false);
		}

	}

	@Override
	public void renderCombatRoomFg(SpriteBatch sb) {
		if(!this.isCamp) {
			for(FireFlyEffect effect : this.fireflies) {
				effect.render(sb);
			}
		}
		sb.setColor(Color.WHITE.cpy());
		sb.draw(topBar, 0, Settings.HEIGHT - (topBar.getHeight() * Settings.scale), 0, 0, Settings.WIDTH, topBar.getHeight(), 1.0f, Settings.scale, 0, 0, 0, topBar.getWidth(), topBar.getHeight(), false, false);

		sb.setColor(Color.WHITE.cpy());
		this.renderAtlasRegionIf(sb, this.fg, true);
		sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE);
		this.renderAtlasRegionIf(sb, this.fgGlow, true);
		sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void renderCampfireRoom(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		this.renderAtlasRegionIf(sb, this.campfireBg, true);
		sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE);
		sb.setColor(new Color(1.0f, 1.0f, 1.0f, MathUtils.cosDeg(System.currentTimeMillis() / 3L % 360L) / 10.0f + 0.8f));
		this.renderQuadrupleSize(sb, this.campfireGlow, !CampfireUI.hidden);
		sb.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);
		sb.setColor(Color.WHITE);
		this.renderAtlasRegionIf(sb, this.campfireKindling, true);
	}

	public static class Tree {
		public float x, y;
		private boolean flip;

		public Tree(float x, float y) {
			this.x = x;
			this.y = y;
			flip = MathUtils.randomBoolean();
		}
	}
}
