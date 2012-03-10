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

(defonce swank* nil)
(defn on-enable [plugin]
  (when (nil? swank*)
    (def swank* (swank.swank/start-repl 4007)))
  (c/lingr "cloyal plugin running..."))
