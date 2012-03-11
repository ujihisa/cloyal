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

(def points (atom {}))

(defn game-end []
  (prn 'cool)
  (prn @points))

(defn timer [sec]
  (if (= sec 0)
    (game-end)
    (do
      (c/broadcast sec " more seconds")
      (future-call #(let [next (int (/ sec 2))]
                      (Thread/sleep (- sec next))
                      (timer next))))))

(defn player-chat-event [evt]
  (let [player (.getPlayer evt)]
    (when (and (.isOp player) (= (.getMessage evt) "start!"))
      (let [world (.getWorld player)]
        (.setTime world 16000)
        (.strikeLightningEffect world (.getLocation player))
        (swap! points (constantly {}))
        (timer 600)))))

(defonce swank* nil)
(defn on-enable [plugin]
  (when (nil? swank*)
    (def swank* (swank.swank/start-repl 4007)))
  (c/lingr "cloyal plugin running..."))
