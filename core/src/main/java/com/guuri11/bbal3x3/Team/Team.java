package com.guuri11.bbal3x3.Team;

import com.guuri11.bbal3x3.Team.player.Player;
import com.guuri11.bbal3x3.Team.player.PlayerOrientation;
import com.guuri11.bbal3x3.Team.player.PlayerStatus;
import java.util.HashMap;

public class Team {
  private final TeamName name;
  // <Player Dorsal, Player>
  private final HashMap<Integer, Player> players = new HashMap<>();

  public Team(final TeamName name) {
    this.name = name;
  }

  public void loadPlayers() {
    float[][] coordinatesA = {
      // Initial jump player
      {908.33704F, 492},
      // The other two player
      {831.94F, 541.9861F},
      {831.94F, 435.04227F}
    };

    float[][] coordinatesB = {
      // Initial jump player
      {955.5491F, 492},
      // The other two player
      {1031.94614F, 541.9861F},
      {1031.94614F, 435.04227F}
    };

    float[][] selectedCoordinates = name == TeamName.A ? coordinatesA : coordinatesB;
    PlayerOrientation orientation =
        name == TeamName.A ? PlayerOrientation.EAST : PlayerOrientation.WEST;

    for (int i = 0; i < selectedCoordinates.length; i++) {
      Player player =
          new Player(
              name,
              selectedCoordinates[i][0], // x
              selectedCoordinates[i][1], // y
              orientation,
              PlayerStatus.IDLE,
              false);
      players.put(i, player);
    }
  }

  public HashMap<Integer, Player> getPlayers() {
    return players;
  }
}
