package studio.ecoprojects.ecozero.economy;

import studio.ecoprojects.ecozero.EcoZero;
import studio.ecoprojects.ecozero.economy.database.EconomyDB;
import studio.ecoprojects.ecozero.economy.shop.Shop;

import java.util.*;

public class Economy {

    private final HashMap<UUID, Double> balances = new HashMap<>();
    private final Shop shop;

    public Economy() {
        shop = new Shop();
    }

    public Double getBalance(UUID uuid) {
        if (balances.get(uuid) == null) {
            if (EconomyDB.getBalance(uuid.toString()).isPresent()) {
                balances.put(uuid, EconomyDB.getBalance(uuid.toString()).get());
            }
        }
        return balances.get(uuid);
    }

    public void setBalance(UUID uuid, Double value) {
        if (isCached(uuid)) {
            balances.replace(uuid, getBalance(uuid), value);
        } else {
            balances.put(uuid, value);
        }

    }

    public Boolean isCached(UUID uuid) {
        return balances.containsKey(uuid);
    }

    public void createAccount(UUID uuid) {
        if (!isCached(uuid)) {
            balances.put(uuid, EcoZero.getPlugin().getConfig().getDouble("starting-balance"));
        }
    }

    public void removeAccount(UUID uuid) {
        balances.remove(uuid);
        EconomyDB.removeAccount(uuid.toString());
    }

    public Set<UUID> getBalanceKeySet() {
        return balances.keySet();
    }

    public Shop getShop() {
        return shop;
    }

}
