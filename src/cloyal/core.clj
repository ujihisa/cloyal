;(ns cloyal.core
;  (:require [swank.swank])
;  (:require [clojure.string :as s])
;  (:import [org.bukkit Bukkit Material])
;  (:import [org.bukkit.entity Animals Arrow Blaze Boat CaveSpider Chicken
;            ComplexEntityPart ComplexLivingEntity Cow Creature Creeper Egg
;            EnderCrystal EnderDragon EnderDragonPart Enderman EnderPearl
;            EnderSignal ExperienceOrb Explosive FallingSand Fireball Fish
;            Flying Ghast Giant HumanEntity Item LightningStrike LivingEntity
;            MagmaCube Minecart Monster MushroomCow NPC Painting Pig PigZombie
;            Player PoweredMinecart Projectile Sheep Silverfish Skeleton Slime
;            SmallFireball Snowball Snowman Spider Squid StorageMinecart
;            ThrownPotion TNTPrimed Vehicle Villager WaterMob Weather Wolf
;            Zombie])
;  (:import [org.bukkit.event.entity CreatureSpawnEvent CreeperPowerEvent
;            EntityChangeBlockEvent
;            EntityCombustByBlockEvent EntityCombustByEntityEvent
;            EntityCombustEvent EntityCreatePortalEvent EntityDamageByBlockEvent
;            EntityDamageByEntityEvent
;            EntityDamageEvent EntityDeathEvent EntityEvent EntityExplodeEvent
;            EntityDamageEvent$DamageCause
;            EntityInteractEvent EntityPortalEnterEvent
;            EntityRegainHealthEvent EntityShootBowEvent EntityTameEvent
;            EntityTargetEvent ExplosionPrimeEvent
;            FoodLevelChangeEvent ItemDespawnEvent ItemSpawnEvent PigZapEvent
;            PlayerDeathEvent PotionSplashEvent ProjectileHitEvent
;            SheepDyeWoolEvent SheepRegrowWoolEvent SlimeSplitEvent])
;  (:require clj-http.client))
(ns cloyal.core
  (:require [cloft.cloft :as c])
  (:require [swank.swank])
  (:require [clojure.string :as s])
  (:import [org.bukkit Bukkit Material])
  (:import [org.bukkit.entity Animals Arrow Blaze Boat CaveSpider Chicken
            ComplexEntityPart ComplexLivingEntity Cow Creature Creeper Egg
            EnderCrystal EnderDragon EnderDragonPart Enderman EnderPearl
            EnderSignal ExperienceOrb Explosive FallingSand Fireball Fish
            Flying Ghast Giant HumanEntity Item LightningStrike LivingEntity
            MagmaCube Minecart Monster MushroomCow NPC Painting Pig PigZombie
            Player PoweredMinecart Projectile Sheep Silverfish Skeleton Slime
            SmallFireball Snowball Snowman Spider Squid StorageMinecart
            ThrownPotion TNTPrimed Vehicle Villager WaterMob Weather Wolf
            Zombie])
  (:import [org.bukkit.event.entity EntityDamageByEntityEvent
            EntityDamageEvent$DamageCause]))

;(def groups (atom {}))
;
;(defn broadcast-groups []
;  (letfn [(unwords [xs]
;            (apply str (interpose " " xs)))]
;    (doseq [[gname players] @groups]
;      (c/broadcast "[" gname "]: " (unwords (map (memfn getDisplayName) players))))))

(def before-lv (atom {}))
(def gaming? (ref false))

(defn player-death-event [evt player]
  (let [lv (.getLevel player)
        new-lv (max 0 (- lv 2))
        drop-exp (* 10 (- lv new-lv))]
    (c/broadcast (.getDisplayName player) " died. dropped " drop-exp "exp")
    (.setNewLevel evt new-lv)
    (.setDroppedExp evt drop-exp)))

(defn entity-death-event [evt]
  (let [entity (.getEntity evt)]
    (cond
      (instance? Player entity) (player-death-event evt entity)
      (and (instance? LivingEntity entity) (.getKiller entity)) nil)))

(defn game-end []
  (dosync (ref-set gaming? false))
  (doseq [player (Bukkit/getOnlinePlayers)]
    (c/broadcast
       (.getDisplayName player)
       ": "
       (+ (double (.getLevel player)) (.getExp player)))
    (.setLevel player (get @before-lv player))))

(defn timer [sec]
  (when @gaming?
    (if (= sec 0)
      (game-end)
      (do
        (if (= sec 1)
          (c/broadcast "1 second")
          (c/broadcast sec " more seconds"))
        (future-call #(let [next (int (/ sec 2))]
                        (Thread/sleep (* (- sec next) 1000))
                        (timer next)))))))

(defn player-chat-event [evt]
  (when (not @gaming?)
    (let [player (.getPlayer evt)]
      (when (and (.isOp player) (= (.getMessage evt) "start!"))
        (let [world (.getWorld player)]
          (.setTime world 13000)
          (.strikeLightningEffect world (.getLocation player))
          (swap! before-lv (constantly {}))
          (doseq [player (Bukkit/getOnlinePlayers)]
            (swap! before-lv assoc player (.getLevel player)))
          (dosync (ref-set gaming? true))
          (timer 480))))))

(defonce swank* nil)
(defn on-enable [plugin]
  (when (nil? swank*)
    (def swank* (swank.swank/start-repl 4007)))
  (c/lingr "cloyal plugin running..."))
