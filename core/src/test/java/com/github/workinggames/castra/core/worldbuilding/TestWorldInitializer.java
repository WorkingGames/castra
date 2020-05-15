package com.github.workinggames.castra.core.worldbuilding;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Paths;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.workinggames.castra.core.font.FontProvider;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerType;
import com.github.workinggames.castra.core.stage.GameConfiguration;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.texture.TextureAtlasInitializer;

public class TestWorldInitializer
{
    private static final float WORLD_WIDTH = 1366;
    private static final float WORLD_HEIGHT = 768;

    private Viewport viewport;
    private TextureAtlas textureAtlas;
    private WorldInitializer worldInitializer;
    private World world;
    private FontProvider fontProvider;

    //    @BeforeMethod
    public void setUp()
    {
        viewport = mock(Viewport.class);
        when(viewport.getWorldWidth()).thenReturn(WORLD_WIDTH);
        when(viewport.getWorldHeight()).thenReturn(WORLD_HEIGHT);

        mockFontProvider();

        textureAtlas = new TextureAtlas();
        new TextureAtlasInitializer()
        {
            @Override
            public FileHandle getFile(String name)
            {
                File file = Paths.get(name).toFile();
                return new FileHandle(file);
            }
        };

        worldInitializer = new WorldInitializer(viewport, textureAtlas);
    }

    private void mockFontProvider()
    {
        File fontFile = Paths.get("target/test-classes/fonts/SoldierCount.fnt").toFile().getAbsoluteFile();
        File imageFile = Paths.get("target/test-classes/fonts/SoldierCount.png").toFile().getAbsoluteFile();
        LwjglFileHandle fontFileHandle = new LwjglFileHandle(fontFile, Files.FileType.Absolute);
        LwjglFileHandle imageFileHandle = new LwjglFileHandle(imageFile, Files.FileType.Absolute);

        BitmapFont font = new BitmapFont(fontFileHandle, imageFileHandle, false);
        fontProvider = mock(FontProvider.class);
        when(fontProvider.getDefaultFont()).thenReturn(font);
        when(fontProvider.getSoldierCountFont()).thenReturn(font);
        when(fontProvider.getSplitInfoFont()).thenReturn(font);
    }

    //    @Test
    public void test()
    {
        World world = mockWorld(1);
        worldInitializer.initialize(world);
    }

    private World mockWorld(long seed)
    {
        Player player1 = mock(Player.class);
        when(player1.getType()).thenReturn(PlayerType.AI);
        Player player2 = mock(Player.class);

        GameConfiguration gameConfiguration = mock(GameConfiguration.class);
        when(gameConfiguration.getSeed()).thenReturn(seed);
        when(gameConfiguration.getPlayer1()).thenReturn(player1);
        when(gameConfiguration.getPlayer2()).thenReturn(player2);
        when(gameConfiguration.isOpponentSettlementDetailsVisible()).thenReturn(false);

        world = new World(viewport, textureAtlas, fontProvider, gameConfiguration);
        return world;
    }
}