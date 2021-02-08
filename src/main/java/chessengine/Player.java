package chessengine;

import java.util.Objects;

public class Player {
    private final PlayerColor playerColor;

    public Player(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public PlayerColor getColor() {
        return this.playerColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return playerColor == player.playerColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerColor);
    }
}
