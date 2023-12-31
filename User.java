List of Users with UserId and Name
List of Logins with UserId and timestamp
Return leaderboard - Name and unique(login),

Example:
Users - [{1,"qw"},{2,"er"}]
Logins - [{1,1},{1,1},{1,3},{2,4},{2,5},{1,7}]
output (Print in descending order based on login attempts)
"qw" : 3
"er" : 2

User "qw" logged 4 times in total, unique attempts - 3. you can assume any input type (either 2d array or List).

Solution :

static class User {
int id;
String name;
  User(int id, String name) {
		this.id = id;
		this.name = name;
	}
}

static class Login {
	int id;
	int timestamp;

	Login(int id, int timestamp) {
		this.id = id;
		this.timestamp = timestamp;
	}
}

static class LeaderBoard implements Comparable<LeaderBoard> {
	String name;
	int uniqueLogin;

	LeaderBoard(String name, int uniqueLogin) {
		this.name = name;
		this.uniqueLogin = uniqueLogin;
	}

	@Override
	public int compareTo(LeaderBoard o2) {
		return o2.uniqueLogin - this.uniqueLogin;
	}
}

public static void main(String[] args) {
	List<User> users = new ArrayList<>();
	users.add(new User(1, "qw"));
	users.add(new User(2, "er"));
	List<Login> logins = new ArrayList<>();
	logins.add(new Login(1, 1));
	logins.add(new Login(1, 1));
	// logins.add(new Login(1, 3));
	logins.add(new Login(2, 4));
	logins.add(new Login(2, 5));
	// logins.add(new Login(1, 7));

	List<LeaderBoard> lists = getLeaderBoard(users, logins);
	lists.stream().forEach(x -> System.out.println(x.name + ":" + x.uniqueLogin));
}

private static List<LeaderBoard> getLeaderBoard(List<User> users, List<Login> logins) {
	List<LeaderBoard> leaderBoardList = new ArrayList<>();
	Map<Integer, Set<Integer>> uniqueLoginMap = new HashMap<>();
	for (Login login : logins) {
		Set<Integer> uniqueTs = uniqueLoginMap.getOrDefault(login.id, new HashSet<>());
		uniqueTs.add(login.timestamp);
		uniqueLoginMap.put(login.id, uniqueTs);
	}

	Map<Integer, String> userMap = new HashMap<>();
	for (User user : users) {
		userMap.put(user.id, user.name);
	}

	for (int id : uniqueLoginMap.keySet()) {
		LeaderBoard leader = new LeaderBoard(userMap.get(id), uniqueLoginMap.get(id).size());
		leaderBoardList.add(leader);
	}
	Collections.sort(leaderBoardList);
	return leaderBoardList;
}


=================================================================================
Used some java17 syntax, hashset and hashmap

public class Leaderboard {
    public static void main(String[] args) {
        List<LeaderboardUser> users = Arrays.asList(new LeaderboardUser(1, "qw"), new LeaderboardUser(2, "er"));
        List<LeaderboardLogin> logins = Arrays.asList(
                new LeaderboardLogin(1, 1),
                new LeaderboardLogin(1, 1),
                new LeaderboardLogin(1, 3),
                new LeaderboardLogin(2, 4),
                new LeaderboardLogin(2, 5),
                new LeaderboardLogin(1, 7)
        );

        Map<Integer, String> userIdToName = new HashMap<>();
        for (LeaderboardUser user : users) {
            userIdToName.put(user.userId, user.name);
        }

        Map<Integer, Set<Integer>> userToUniqueLogins = new HashMap<>();
        for (LeaderboardLogin login : logins) {
            userToUniqueLogins
                    .computeIfAbsent(login.userId, k -> new HashSet<>())
                    .add(login.timestamp);
        }

        List<Map.Entry<String, Integer>> leaderboard = new ArrayList<>();
        for (Map.Entry<Integer, Set<Integer>> entry : userToUniqueLogins.entrySet()) {
            leaderboard.add(new AbstractMap.SimpleEntry<>(userIdToName.get(entry.getKey()), entry.getValue().size()));
        }

        leaderboard.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (Map.Entry<String, Integer> entry : leaderboard) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    record LeaderboardUser(int userId, String name) {}
    record LeaderboardLogin(int userId, int timestamp) {}
}
