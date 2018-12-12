
public class EventDeck {
    Event[] eventArray;
    GameCourt court;
    
    public EventDeck(GameCourt court) {
    	this.court = court;
        this.eventArray = new Event[] {
                // name, description, warning, filename
                // death, warn, mig, lose,
                // water, jungle, tundra, sleep, 
                // probability
                
                new Event("Nothing", "", "", "Event-Nothing.jpg",
                            false, false, false, false,
                            false, false, false, false, 
                            1.0f),
                
                new Event("Flood", "The beaches are flooded!", 
                        "The water is rising...", "Event-Flood.jpg",
                        true, true, false, false,
                        true, false, false, false,
                        0.1f) {
                	@Override
                	public void doEffect() {
                		for(int i = 0; i < court.board.board.length; i++) {
                			for(int j = 0; j < court.board.board[i].length; j++) {
                				if(court.board.board[i][j].type == BoardTile.EnumTileType.BEACH) {
                    				court.board.board[i][j].type = BoardTile.EnumTileType.BEACH_FLOODED;
                    				court.board.board[i][j].turnsUntilBeach = 2;
                				}
                			}
                		}
                	}
                },
                
                new Event("Blizzard", "If you don't have the ability to move through tundra, die.", 
                        "It's getting colder...", "Event-Blizzard.jpg",
                        true, true, false, false,
                        false, false, true, false,
                        .1f) {
                	@Override
                	public void doEffect() {
                		for(int i = 0; i < court.players.length; i++) {
                			Player currPlay = court.players[i];
                			for(int j = 0; j < 3; j++) {
                				if((currPlay.traits[j] != null && currPlay.traits[j].tundraMovement == false) || currPlay.traits[j] == null) {
                					System.out.println("Player dies from tundra!");
                					currPlay.die(null);
                				}
                			}
                		}
                		for(int i = 0; i < court.enemies.size(); i++) {
                			Enemy currPlay = court.enemies.get(i);
                			for(int j = 0; j < currPlay.traits.length; j++) {
                				if(currPlay.traits[j] != null && currPlay.traits[j].tundraMovement == false) {
                					currPlay.die(null);
                				}
                			}
                		}
                	}
                },
                
                new Event("Volcanic Erruption", "You're all dead!", 
                        "The ground is rumbling...", "Event-Volcano.jpg",
                        true, true, false, false,
                        false, false, false, false,
                        .1f) {
                	@Override
                	public void doEffect() {
                		for(int i = 0; i < court.players.length; i++) {
                			court.players[i].die(null);
                		}
                		for(int i = 0; i < court.enemies.size(); i++) {
                			court.enemies.get(i).die(null);
                		}
                	}
                },
                
                new Event("Meteor", "You're all dead!", 
                        "", "Event-Meteor.jpg",
                        true, false, false, false,
                        false, false, false, false,
                        .1f) {
                	@Override
                	public void doEffect() {
                		for(int i = 0; i < court.players.length; i++) {
                			court.players[i].die(null);
                			//court.players[i].update();
                		}
                		for(int i = 0; i < court.enemies.size(); i++) {
                			court.enemies.get(i).die(null);
                			//court.enemies.get(i).update();
                		}
                	}
                },
                
                new Event("Migration", "Move your home token to any used nest.", 
                        "", "Event-Migration.jpg",
                        false, false, true, false,
                        false, false, false, false,
                        .0f) {
                	@Override
                	public void doEffect() {
                		
                	}
                },
                
                new Event("Famine", "If you do not reach a forest or kill another player this turn, you die.", 
                        "", "Event-Famine.jpg",
                        true, false, false, false,
                        false, true, false, false,
                        .0f) {
                	@Override
                	public void doEffect() {
                		
                	}
                },
                
                new Event("Night", "If you do not have nocturnalism, lose a turn.", 
                        "It's getting darker...", "Event-Night.jpg",
                        false, false, false, true,
                        false, false, false, true,
                        .1f) {
                	@Override
                	public void doEffect() {
                		for(int i = 0; i < court.players.length; i++) {
                			Player currPlay = court.players[i];
                			for(int j = 0; j < 3; j++) {
                				if((currPlay.traits[j] != null && currPlay.traits[j].nocturnalism == false) || currPlay.traits[j] == null) {
                					currPlay.loseTurn = true;
                				}
                			}
                		}
                		for(int i = 0; i < court.enemies.size(); i++) {
                			Enemy currPlay = court.enemies.get(i);
                			for(int j = 0; j < currPlay.traits.length; j++) {
                				if(currPlay.traits[j] != null && currPlay.traits[j].nocturnalism == false) {
                					currPlay.loseTurn = true;
                				}
                			}
                		}
                	}
                }
        };
    }
    
    public Event randomEvent() {
        float sumProbabilities = 0.f;
        for(int i = 0; i < eventArray.length; i++) {
            sumProbabilities += eventArray[i].probability;
        }
        
        double rand = Math.random() * sumProbabilities;
        
        sumProbabilities = 0.f;
        for(int i = 0; i < eventArray.length; i++) {
            sumProbabilities += eventArray[i].probability;
            if(rand < sumProbabilities) {
                return eventArray[i];
            }
        }
        
        return eventArray[0];
    }
}
