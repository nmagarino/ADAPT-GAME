
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
                            .0f),
                
                new Event("Flood", "The beaches are flooded!", 
                        "The water is rising...", "Event-Flood.jpg",
                        true, true, false, false,
                        true, false, false, false,
                        .1f) {
                	@Override
                	public void doEffect() {
                		
                	}
                },
                
                new Event("Blizzard", "If you don’t have the ability to move through tundra, die.", 
                        "It's getting colder...", "Event-Blizzard.jpg",
                        true, true, false, false,
                        false, false, true, false,
                        .1f),
                
                new Event("Volcanic Erruption", "You're all dead!", 
                        "The volcano seems to be waking up....", "Event-Volcano.jpg",
                        true, true, false, false,
                        false, false, false, false,
                        .1f),
                
                new Event("Meteor", "You're all dead!", 
                        "", "Event-Meteor.jpg",
                        true, false, false, false,
                        false, false, false, false,
                        .1f),
                
                new Event("Migration", "Move your home token to any used nest.", 
                        "", "Event-Migration.jpg",
                        false, false, true, false,
                        false, false, false, false,
                        .1f),
                
                new Event("Famine", "If you do not reach a forest or kill another player this turn, you die.", 
                        "", "Event-Famine.jpg",
                        true, false, false, false,
                        false, true, false, false,
                        .1f),
                
                new Event("Night", "If you do not have nocturnalism, lose a turn.", 
                        "", "Event-Night.jpg",
                        false, false, false, true,
                        false, false, false, true,
                        .1f)
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
