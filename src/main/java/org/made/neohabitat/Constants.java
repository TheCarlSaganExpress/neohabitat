package org.made.neohabitat;

/**
 * The PL1 Habitat application was monolithic and depended heavily on symbolic
 * constants throughout. These are implement here as public static final values,
 * but keep their original names. Every Habitat Mod has access to these "global
 * constants" through the HabitatMod abstract superclass.
 * 
 * @author randy
 *
 */

public interface Constants {
    
    /* Some Java-Server only constants */
    
    public static final int     CONNECTION_JSON             = 0;
    public static final int     CONNECTION_HABITAT          = 0;
    public static final int     MAX_HEALTH                  = 255;

    public static final int     BOOK$HELP                   = 0;
    public static final int     BOOK$VENDO                  = 1;
    public static final int     PAPER$HELP                  = 2;
    
    public static final int     SAFE_X                      = 8;
    public static final int     SAFE_Y                      = 132;
    
    /*
     * defs_class.incl.pl1
     *
     * Assign class numbers to classes.
     *
     * Chip Morningstar Lucasfilm Ltd. 8-April-1986
     *
     */
    
    public static final int      CLASS_AMULET               = 2;
    public static final int      CLASS_GHOST                = 3;
    public static final int      CLASS_AQUARIUM             = 129;
    public static final int      CLASS_ATM                  = 4;
    public static final int      CLASS_AVATAR               = 1;
    public static final int      CLASS_BAG                  = 6;
    public static final int      CLASS_BALL                 = 7;
    public static final int      CLASS_BED                  = 130;
    public static final int      CLASS_BEEPER               = 8;
    public static final int      CLASS_BOAT                 = 9;
    public static final int      CLASS_BOOK                 = 10;
    public static final int      CLASS_BOOMERANG            = 11;
    public static final int      CLASS_BOTTLE               = 12;
    public static final int      CLASS_BOX                  = 13;
    public static final int      CLASS_BRIDGE               = 131;
    public static final int      CLASS_BUILDING             = 132;
    public static final int      CLASS_BUREAUCRAT           = 158;
    public static final int      CLASS_BUSH                 = 133;
    public static final int      CLASS_CAR                  = 15;
    public static final int      CLASS_CHAIR                = 134;
    public static final int      CLASS_CHEST                = 135;
    public static final int      CLASS_CLUB                 = 16;
    public static final int      CLASS_COKE_MACHINE         = 136;
    public static final int      CLASS_COMPASS              = 17;
    public static final int      CLASS_COUCH                = 137;
    public static final int      CLASS_COUNTERTOP           = 18;
    public static final int      CLASS_CREDIT_CARD          = 19;
    public static final int      CLASS_CRYSTAL_BALL         = 20;
    public static final int      CLASS_DIE                  = 21;
    public static final int      CLASS_DISPLAY_CASE         = 22;
    public static final int      CLASS_DOOR                 = 23;
    public static final int      CLASS_DROPBOX              = 24;
    public static final int      CLASS_DRUGS                = 25;
    public static final int      CLASS_ELEVATOR             = 28;
    public static final int      CLASS_ESCAPE_DEV           = 26;
    public static final int      CLASS_FAKE_GUN             = 27;
    public static final int      CLASS_FENCE                = 138;
    public static final int      CLASS_FLAG                 = 29;
    public static final int      CLASS_FLASHLIGHT           = 30;
    public static final int      CLASS_FLOOR_LAMP           = 139;
    public static final int      CLASS_FORTUNE_MACHINE      = 140;
    public static final int      CLASS_FOUNTAIN             = 141;
    public static final int      CLASS_FRISBEE              = 31;
    public static final int      CLASS_GAME_PIECE           = 5;
    public static final int      CLASS_GARBAGE_CAN          = 32;
    public static final int      CLASS_GLUE                 = 98;
    public static final int      CLASS_GEMSTONE             = 33;
    public static final int      CLASS_ROAD_PIZZA           = 34;
    public static final int      CLASS_GRENADE              = 35;
    public static final int      CLASS_GROUND               = 36;
    public static final int      CLASS_GUN                  = 37;
    public static final int      CLASS_HAND_OF_GOD          = 38;
    public static final int      CLASS_HAT                  = 39;
    public static final int      CLASS_HEAD                 = 127;
    public static final int      CLASS_HOUSE_CAT            = 143;
    public static final int      CLASS_HOT_TUB              = 144;
    public static final int      CLASS_INSTANT_OBJECT       = 40;
    public static final int      CLASS_JACKET               = 41;
    public static final int      CLASS_JUKEBOX              = 145;
    public static final int      CLASS_KEY                  = 42;
    public static final int      CLASS_KNICK_KNACK          = 43;
    public static final int      CLASS_KNIFE                = 44;
    public static final int      CLASS_MAGIC_LAMP           = 45;
    public static final int      CLASS_MAGIC_STAFF          = 46;
    public static final int      CLASS_MAGIC_WAND           = 47;
    public static final int      CLASS_MAILBOX              = 48;
    public static final int      CLASS_MATCHBOOK            = 49;
    public static final int      CLASS_MICROPHONE           = 50;
    public static final int      CLASS_MOTORCYCLE           = 51;
    public static final int      CLASS_MOVIE_CAMERA         = 52;
    public static final int      CLASS_PAWN_MACHINE         = 96;
    public static final int      CLASS_PAPER                = 54;
    public static final int      CLASS_PLAQUE               = 55;
    public static final int      CLASS_PARKING_METER        = 146;
    public static final int      CLASS_PENCIL               = 55;
    public static final int      CLASS_SHORT_SIGN           = 56;
    public static final int      CLASS_PICTURE              = 152;
    public static final int      CLASS_PLANT                = 58;
    public static final int      CLASS_POND                 = 147;
    public static final int      CLASS_RADIO                = 59;
    public static final int      CLASS_REGION               = 0;
    public static final int      CLASS_RING                 = 60;
    public static final int      CLASS_RIVER                = 148;
    public static final int      CLASS_ROCK                 = 61;
    public static final int      CLASS_ROOF                 = 149;
    public static final int      CLASS_RUBBER_DUCKY         = 62;
    public static final int      CLASS_SAFE                 = 150;
    public static final int      CLASS_SECURITY_DEV         = 63;
    public static final int      CLASS_SENSOR               = 64;
    public static final int      CLASS_SEX_CHANGER          = 90;
    public static final int      CLASS_MAGIC_IMMOBILE       = 97;
    public static final int      CLASS_SHOES                = 66;
    public static final int      CLASS_SIDEWALK             = 151;
    public static final int      CLASS_SIGN                 = 57;
    public static final int      CLASS_SKATEBOARD           = 67;
    public static final int      CLASS_SKIRT                = 68;
    public static final int      CLASS_SKY                  = 69;
    public static final int      CLASS_SPRAY_CAN            = 95;
    public static final int      CLASS_STEREO               = 70;
    public static final int      CLASS_STREET               = 153;
    public static final int      CLASS_STREETLAMP           = 154;
    public static final int      CLASS_STUN_GUN             = 91;
    public static final int      CLASS_TABLE                = 155;
    public static final int      CLASS_TAPE                 = 71;
    public static final int      CLASS_TEDDY_BEAR           = 72;
    public static final int      CLASS_TELEPHONE            = 73;
    public static final int      CLASS_TELEPORT             = 74;
    public static final int      CLASS_TICKET               = 75;
    public static final int      CLASS_TOKENS               = 76;
    public static final int      CLASS_TRAPEZOID            = 87;
    public static final int      CLASS_SUPER_TRAPEZOID      = 92;
    public static final int      CLASS_FLAT                 = 93;
    public static final int      CLASS_TEST                 = 94;
    public static final int      CLASS_TOWEL                = 77;
    public static final int      CLASS_TREE                 = 156;
    public static final int      CLASS_TRUCK                = 78;
    public static final int      CLASS_WALKIE_TALKIE        = 79;
    public static final int      CLASS_WALL                 = 80;
    public static final int      CLASS_WATER                = 81;
    public static final int      CLASS_WINDOW               = 157;
    public static final int      CLASS_WINDUP_TOY           = 82;
    public static final int      CLASS_FISH_FOOD            = 83;
    public static final int      CLASS_CHANGOMATIC          = 84;
    public static final int      CLASS_VENDO_FRONT          = 85;
    public static final int      CLASS_VENDO_INSIDE         = 86;
    public static final int      CLASS_HOLE                 = 88;
    public static final int      CLASS_SHOVEL               = 89;
    
    /*
     * microcosm.incl.pl1
     *
     * General purpose include file for MicroCosm(TM).
     *
     * Chip Morningstar Lucasfilm Ltd. 8-April-1986
     */
    public static final int      NULL                       = 0;
    public static final int      FALSE                      = 0;
    public static final int      BOING_FAILURE              = 2;
    public static final int      TRUE                       = 1;
    /* public static final int       false       = '0'b omitted to avoid conflict. FRF */
    /* public static final int       true        = '1'b omitted to avoid conflict. FRF */
    public static final int      TEXT_LENGTH                = 256;
    public static final int      PAPER_LENGTH               = 640;
    
    // y position bits
    public static final int      FOREGROUND_BIT             = 0b10000000;
    // orientation bits
    public static final int      FACING_BIT                 = 0b00000001;
    public static final int      PATTERN_BITS               = 0b01111000;
    public static final int      COLOR_BITS                 = 0b01111000;
    public static final int      COLOR_FLAG                 = 0b10000000;
    public static final int      BYTE_MASK                  = 0b11111111;
    public static final int      ORIENTATION_BIT            = 1;
    
    /* Avatar constants */
    public static final int      MAIL_SLOT                  = 4;
    public static final int      HANDS                      = 5;
    public static final int      HEAD                       = 6;
    public static final int      AVATAR_CAPACITY            = 8;
    public static final int      UNWEARABLE                 = 0;                                                      /*
                                                                                                                       * historical
                                                                                                                       * aberration
                                                                                                                       */
    
    /* Container constants */
    public static final int      OPEN_BIT                   = 1;
    public static final int      UNLOCKED_BIT               = 2;
    
    /* Curse constants */
    public static final int      CURSE_NONE                 = 0;
    public static final int      CURSE_COOTIES              = 1;
    public static final int      CURSE_SMILEY               = 2;
    public static final int      CURSE_MUTANT               = 3;
    public static final int      CURSE_FLY                  = 4;
    
    public static final int      HEAD_COOTIE                = 117;
    public static final int      HEAD_SMILEY                = 110;
    public static final int      HEAD_MUTANT                = 27;
    public static final int      HEAD_FLY                   = 13;
    
    /* Magic lamp constants */
    public static final int      MAGIC_LAMP_WAITING         = 0;
    public static final int      MAGIC_LAMP_GENIE           = 1;
    
    /* Weapon constants */
    /* The total amount of damage to be rendered by an ATTACK. */
    public static final int      DAMAGE_DECREMENT           = 20;
    /* no effect, beep at player */
    public static final int      MISS                       = 0;
    /* destroy object that is target */
    public static final int      DESTROY                    = 1;
    /* keester avatar that is target */
    public static final int      HIT                        = 2;
    /* kill avatar that is target */
    public static final int      DEATH                      = 3;
    public static final int      GRENADE_FUSE_DELAY         = 20;
    public static final int      GET_SHOT_POSTURE           = 138;
    
    /* Drugs constants */       
    public static final int      NUMBER_OF_DRUG_EFFECTS     = 3;
    
    /* general flag constants */
    public static final int      RESTRICTED                 = 1;
    public static final int      MODIFIED                   = 2;
    
    /* region nitty_bits constants */
    public static final int      WEAPONS_FREE               = 1;
    public static final int      STEAL_FREE                 = 2;
    
    /* avatar nitty_bit constants */
    public static final int      CURSE_IMMUNITY_BIT         = 31;
    public static final int      VOTED_FLAG                 = 3;
    public static final int      GOD_FLAG                   = 4;
    public static final int      MISC_FLAG1                 = 5;
    public static final int      MISC_FLAG2                 = 6;
    public static final int      MISC_FLAG3                 = 7;
    /* avatar NEOHabitat nitty_bit flags start at the top and work down. */
    public static final int      INTENTIONAL_GHOST          = 31;
    
    /* object nitty-bits constants */
    public static final int      DOOR_AVATAR_RESTRICTED_BIT = 32;
    public static final int      DOOR_GHOST_RESTRICTED_BIT  = 31;
    
    public static final int      UNASSIGNED_NOID            = 256;
    public static final int      THE_REGION                 = 0;
    public static final int      GHOST_NOID                 = 255;

    public static final int      ObjectsPerRegion           = 255;
    public static final int      UsersPerRegion             = 6;
    public static final int      regions_per_process        = 10;
    
    public static final int      MAX_CLASS_NUMBER           = 255;
    public static final int      MAX_CLASS_NUMBER_PLUS_1    = 256;
    public static final int      NUMBER_OF_RESOURCES        = 687;
    
    public static final int      Separation_Char            = 144;
    
    public static final int      C64_HEAP_SIZE              = 16244;
                                                                                                                
    public static final int      FIRST                      = 3;
    public static final int      SECOND                     = 4;
    public static final int      THIRD                      = 5;
    public static final int      FOURTH                     = 6;
    public static final int      FIFTH                      = 7;
    
    public static final int      COLLISION_ON               = 0b00000001;
    public static final int      ADJACENCY_ON               = 0b00000001;
    
    public static final int      C64_XPOS_OFFSET            = 7;
    public static final int      C64_YPOS_OFFSET            = 8;
    public static final int      C64_ORIENT_OFFSET          = 9;
    public static final int      C64_GR_STATE_OFFSET        = 10;
    public static final int      C64_CONTAINED_OFFSET       = 11;
    public static final int      C64_TOKEN_DENOM_OFFSET     = 15;
    public static final int      C64_TEXT_OFFSET            = 15;
    public static final int      C64_CUSTOMIZE_OFFSET       = 26;
    public static final int      C64_DESTX_OFFSET           = 28;
    public static final int      C64_DESTY_OFFSET           = 29;
    
    public static final int      OPERATE                    = 152;
    
    public static final int      WALK_ENTRY                 = 0;
    public static final int      TELEPORT_ENTRY             = 1;
    public static final int      DEATH_ENTRY                = 2;
    
    public static final int      WEST                       = 0;
    public static final int      EAST                       = 1;
    public static final int      NORTH                      = 2;
    public static final int      SOUTH                      = 3;
    public static final int      AUTO_TELEPORT_DIR          = 4;
    
    public static final int      MAP_NORTH                  = 0;
    public static final int      MAP_EAST                   = 1;
    public static final int      MAP_SOUTH                  = 2;
    public static final int      MAP_WEST                   = 3;
    
    public static final int      SIT_GROUND                 = 132;
    public static final int      SIT_CHAIR                  = 133;
    public static final int      SIT_FRONT                  = 157;
    public static final int      STAND_FRONT                = 146;
    public static final int      STAND_LEFT                 = 251;
    public static final int      STAND_RIGHT                = 252;
    public static final int      STAND                      = 129;
    public static final int      FACE_LEFT                  = 254;
    public static final int      FACE_RIGHT                 = 255;
    
    public static final int      COLOR_POSTURE              = 253;
    
    static final int             HEAD_GROUND_STATE          = 2;
    static final int             GROUND_FLAT                = 2;
    static final int             INVISIBLE                  = 0b01000000;
    
    /*
     * defs_message.incl.pl1
     *
     * Include file defining MicroCosm message numbers for various messages.
     *
     * Chip Morningstar Lucasfilm Ltd. 8-April-1986
     */
    
    /* Messages from the home system to the host system. */
    public static final int      ASK                        = 4;
    public static final int      ATTACK                     = 5;
    public static final int      BASH                       = 6;
    public static final int      BUGOUT                     = 4;
    public static final int      CATALOG                    = 5;
    public static final int      CHANGE                     = 4;
    public static final int      CLOSE                      = 4;
    public static final int      CLOSECONTAINER             = 4;
    public static final int      CUSTOMIZE                  = 4;
    public static final int      DESCRIBE                   = 1;
    public static final int      DEPOSIT                    = 1;
    public static final int      DIRECT                     = 4;
    public static final int      DIG                        = 4;
    public static final int      ESP                        = 11;
    public static final int      FAKESHOOT                  = 4;
    public static final int      FEED                       = 4;
    public static final int      FILL                       = 4;
    public static final int      FINGER_IN_QUE              = 5;
    public static final int      FLUSH                      = 6;
    public static final int      FNKEY                      = 14;
    public static final int      GET                        = 1;
    public static final int      GRAB                       = 4;
    public static final int      HAND                       = 5;
    public static final int      HELP                       = 0;
    public static final int      I_AM_HERE                  = 6;
    public static final int      IMALIVE                    = 3;
    public static final int      CORPORATE                  = 10;
    public static final int      DISCORPORATE               = 10;
    public static final int      LEAVE                      = 2;
    public static final int      LOAD                       = 6;
    public static final int      MAGIC                      = 4;
    public static final int      MUNCH                      = 6;
    public static final int      NEWREGION                  = 9;
    public static final int      OFF                        = 4;
    public static final int      OFFPLAYER                  = 4;
    public static final int      ON                         = 5;
    public static final int      ONPLAYER                   = 5;
    public static final int      OPEN                       = 5;
    public static final int      OPENCONTAINER              = 5;
    public static final int      PAY                        = 4;
    public static final int      PAYTO                      = 4;
    public static final int      PLAYMESSAGE                = 4;
    public static final int      POSTURE                    = 6;
    public static final int      POUR                       = 5;
    public static final int      PROMPT_REPLY               = 7;
    public static final int      PULLPIN                    = 4;
    public static final int      PUT                        = 2;
    public static final int      READ                       = 4;
    public static final int      READLABEL                  = 4;
    public static final int      READMAIL                   = 4;
    public static final int      README                     = 4;
    public static final int      REMOVE                     = 7;
    public static final int      RESET                      = 5;
    public static final int      ROLL                       = 4;
    public static final int      RUB                        = 4;
    public static final int      SCAN                       = 4;
    public static final int      SELECT                     = 6;
    public static final int      SENDMAIL                   = 5;
    public static final int      PSENDMAIL                  = 6;
    public static final int      SEXCHANGE                  = 4;
    public static final int      SIT                        = 12;
    public static final int      SPEAK                      = 7;
    public static final int      SPRAY                      = 4;
    public static final int      STUN                       = 5;
    public static final int      TAKE                       = 4;
    public static final int      TALK                       = 8;
    public static final int      THROW                      = 3;
    public static final int      TOUCH                      = 13;
    public static final int      UNHOOK                     = 9;
    public static final int      UNLOAD                     = 7;
    public static final int      VSELECT                    = 5;
    public static final int      WALK                       = 8;
    public static final int      WEAR                       = 6;
    public static final int      WIND                       = 4;
    public static final int      WISH                       = 5;
    public static final int      WITHDRAW                   = 2;
    public static final int      WRITE                      = 5;
    public static final int      ZAPTO                      = 5;
    
    /* Messages from the host system to the home system. */
    public static final int      ANNOUNCE_$                 = 10;
    public static final int      APPEARING_$                = 18;
    public static final int      ARRIVAL_$                  = 9;
    public static final int      ATTACK$                    = 9;
    public static final int      AUTO_TELEPORT_$            = 21;
    public static final int      BASH$                      = 10;
    public static final int      BEEP$                      = 8;
    public static final int      BLAST$                     = 8;
    public static final int      CAUGHT_UP_$                = 17;
    public static final int      CHANGE$                    = 8;
    public static final int      CHANGE_CONTAINERS_$        = 19;
    public static final int      BUGOUT$                    = 8;
    public static final int      CHANGESTATE$               = 8;
    public static final int      CHANGESTATE_$              = 8;
    public static final int      CLOSE$                     = 12;
    public static final int      CLOSECONTAINER$            = 13;
    public static final int      DEPARTING_$                = 10;
    public static final int      DEPARTURE_$                = 11;
    public static final int      DIAL$                      = 10;
    public static final int      DIE$                       = 11;
    public static final int      DIG$                       = 8;
    public static final int      DRIVE$                     = 8;
    public static final int      EXPIRE_$                   = 9;
    public static final int      EXPLODE_$                  = 8;
    public static final int      FAKESHOOT$                 = 8;
    public static final int      FIDDLE_$                   = 12;
    public static final int      FILL$                      = 8;
    public static final int      FLUSH$                     = 8;
    public static final int      GET$                       = 15;
    public static final int      GOAWAY_$                   = 9;
    public static final int      GRAB$                      = 16;
    public static final int      GRABFROM$                  = 17;
    public static final int      HANG$                      = 11;
    public static final int      HEREIS_$                   = 8;
    public static final int      HUNGUP$                    = 12;
    public static final int      LOAD$                      = 8;
    public static final int      MAILARRIVED$               = 8;
    public static final int      MUNCH$                     = 8;
    public static final int      NEWHEAD$                   = 31;
    public static final int      OBJECTSPEAK_$              = 15;
    public static final int      OFF$                       = 8;
    public static final int      OFFLIGHT$                  = 8;
    public static final int      ON$                        = 9;
    public static final int      ONLIGHT$                   = 9;
    public static final int      OPEN$                      = 18;
    public static final int      OPENCONTAINER$             = 19;
    public static final int      ORACLESPEAK_$              = 8;
    public static final int      PAID$                      = 30;
    public static final int      PAY$                       = 8;
    public static final int      PAYTO$                     = 8;
    public static final int      PLAY_$                     = 14;
    public static final int      POSTURE$                   = 20;
    public static final int      POUR$                      = 9;
    public static final int      PROMPT_USER_$              = 20;
    public static final int      PUT$                       = 22;
    public static final int      REINCARNATE$               = 23;
    public static final int      REMOVE$                    = 29;
    public static final int      RESET$                     = 9;
    public static final int      RETURN$                    = 1;
    public static final int      CHANGELIGHT_$              = 13;
    public static final int      ROLL$                      = 8;
    public static final int      RUB$                       = 9;
    public static final int      SCAN$                      = 8;
    public static final int      SELL$                      = 9;
    public static final int      SEXCHANGE$                 = 8;
    public static final int      SIT$                       = 16;
    public static final int      SPEAK$                     = 14;
    public static final int      SPEAKFORTUNE$              = 10;
    public static final int      SPRAY$                     = 8;
    public static final int      TAKE$                      = 8;
    public static final int      TAKEMESSAGE$               = 8;
    public static final int      THROW$                     = 24;
    public static final int      THROWAWAY$                 = 8;
    public static final int      TRANSFORM$                 = 8;
    public static final int      UNHOOK$                    = 15;
    public static final int      UPDATE$                    = 11;
    public static final int      UNLOAD$                    = 8;
    public static final int      VSELECT$                   = 8;
    public static final int      WAITFOR_$                  = 16;
    public static final int      WALK$                      = 8;
    public static final int      WEAR$                      = 28;
    public static final int      WIND$                      = 8;
    public static final int      WISH$                      = 8;
    public static final int      ZAPIN$                     = 9;
    public static final int      ZAPTO$                     = 10;
    
    /* Avatar Gestures */
    public static final int      AV_ACT_stand               = 0x80 + 1;
    public static final int      AV_ACT_walk                = 0x80 + 2;
    public static final int      AV_ACT_hand_back           = 0x80 + 3;
    public static final int      AV_ACT_sit_floor           = 0x80 + 4;
    public static final int      AV_ACT_sit_chair           = 0x80 + 5;
    public static final int      AV_ACT_bend_over           = 0x80 + 6;
    public static final int      AV_ACT_bend_back           = 0x80 + 7;
    public static final int      AV_ACT_point               = 0x80 + 8;
    public static final int      AV_ACT_throw               = 0x80 + 9;
    public static final int      AV_ACT_get_shot            = 0x80 + 10;
    public static final int      AV_ACT_jump                = 0x80 + 11;
    public static final int      AV_ACT_punch               = 0x80 + 12;
    public static final int      AV_ACT_wave                = 0x80 + 13;
    public static final int      AV_ACT_frown               = 0x80 + 14;
    public static final int      AV_ACT_stand_back          = 0x80 + 15;
    public static final int      AV_ACT_walk_front          = 0x80 + 16;
    public static final int      AV_ACT_walk_back           = 0x80 + 17;
    public static final int      AV_ACT_stand_front         = 0x80 + 18;
    public static final int      AV_ACT_unpocket            = 0x80 + 19;
    public static final int      AV_ACT_gimme               = 0x80 + 20;
    public static final int      AV_ACT_knife               = 0x80 + 21;
    public static final int      AV_ACT_arm_get             = 0x80 + 22;
    public static final int      AV_ACT_hand_out            = 0x80 + 23;
    public static final int      AV_ACT_operate             = 0x80 + 24;
    public static final int      AV_ACT_arm_back            = 0x80 + 25;
    public static final int      AV_ACT_shoot1              = 0x80 + 26;
    public static final int      AV_ACT_shoot2              = 0x80 + 27;
    public static final int      AV_ACT_nop                 = 0x80 + 28;
    public static final int      AV_ACT_sit_front           = 0x80 + 29;
    
    /* Prompt strings */
    public static final String   GOD_TOOL_PROMPT            = "Edit:";
    public static final String   MAGIC_OPEN_PROMPT          = "Yes?";
    public static final String   LOTTO_PROMPT               = "Enter your 3 digit number:";
    public static final int      MAX_WORD_BALLON_LEN        = 80;
    
    // special characters in the character set:
    // D = Down U = Up L = Left R = Right C = Center
    
    public static final int      DIAG_DR                    = 9;
    public static final int      DIAG_DL                    = 31;                                                     // 10
                                                                                                                      // is
                                                                                                                      // linefeed!
    public static final int      DIAG_UL                    = 11;
    public static final int      DIAG_UR                    = 12;
    public static final int      BOX_DR                     = 13;
    public static final int      BOX_DL                     = 14;
    public static final int      BOX_UL                     = 15;
    public static final int      BOX_UR                     = 16;
    public static final int      CIRC_DR                    = 17;
    public static final int      CIRC_DL                    = 18;
    public static final int      CIRC_UL                    = 19;
    public static final int      CIRC_UR                    = 20;
    public static final int      BOX_CR                     = 21;
    public static final int      BOX_CL                     = 22;
    public static final int      BOX_CU                     = 23;
    public static final int      BOX_CD                     = 24;
    public static final int      BOX_C                      = 25;
    public static final int      LINE_UD                    = 26;
    public static final int      LINE_LR                    = 27;
    public static final int      BALL                       = 28;
    public static final int      BALL_FILLED                = 29;
    public static final int      BALL_ALT                   = 30;
    public static final int      ARROW_U                    = 124;
    public static final int      ARROW_D                    = 125;
    public static final int      ARROW_L                    = 126;
    public static final int      ARROW_R                    = 127;
    public static final int      SQUIGGLE                   = 123;
    public static final int      BLACK_CHAR                 = 96;
    
    // Sign characters
    
    public static final int      shift_0                    = 128 + 0;
    public static final int      shift_plus                 = 128 + 1;
    public static final int      shift_minus                = 128 + 2;
    public static final int      shift_asterisk             = 128 + 3;
    public static final int      shift_at                   = 128 + 4;
    public static final int      shift_equal                = 128 + 5;
    public static final int      sign_char_return_key       = 128 + 6;
    public static final int      cursor_right               = 128 + 7;
    public static final int      cursor_left                = 128 + 8;
    public static final int      cursor_up                  = 128 + 9;
    public static final int      cursor_down                = 128 + 10;
    public static final int      home_key                   = 128 + 11;
    public static final int      clear_key                  = 128 + 12;
    public static final int      british_pound              = 128 + 13;
    public static final int      insert_key                 = 128 + 14;
    public static final int      shift_british_pound        = 128 + 15;
    
    public static final int      start_text                 = insert_key;
    public static final int      sign_char_half_space       = shift_0;
    public static final int      sign_char_double_space     = shift_british_pound;
    public static final int      sign_char_inc_width        = shift_plus;
    public static final int      sign_char_dec_width        = shift_minus;
    public static final int      sign_char_inc_height       = shift_asterisk;
    public static final int      sign_char_dec_height       = shift_at;
    public static final int      sign_char_half_size        = shift_equal;
    public static final int      sign_char_half_char_down   = home_key;
    public static final int      sign_char_inverse_video    = clear_key;
    public static final int      sign_char_cursor_right     = cursor_right;
    public static final int      sign_char_cursor_left      = cursor_left;
    public static final int      sign_char_cursor_up        = cursor_up;
    public static final int      sign_char_cursor_down      = cursor_down;

    /** This prefix is to always be displayed before a feature that did not exist in either Lucasfilm's Habitat Beta or Club Caribe */
    public static final String   UPGRADE_PREFIX             = "" + (char) ARROW_R;
    
    public static final long     ONE_DAY                    = 1000 * 60 * 60 * 24;  // Millis
    
    public static final int      HS$lifetime                =   1;
    public static final int      HS$max_lifetime            =   2;
    public static final int      HS$deaths                  =   3;
    public static final int      HS$treasures               =   4;
    public static final int      HS$mail_send_count         =   5;
    public static final int      HS$mail_recv_count         =   6;
    public static final int      HS$grabs                   =   7;
    public static final int      HS$kills                   =   8;
    public static final int      HS$escapes                 =   9;
    public static final int      HS$body_changes            =  10;
    public static final int      HS$max_wealth              =  11;
    public static final int      HS$travel                  =  12;
    public static final int      HS$max_travel              =  13;
    public static final int      HS$teleports               =  14;
    public static final int      HS$explored                =  15;
    public static final int      HS$online_time             =  16;
    public static final int      HS$talkcount               =  17;
    public static final int      HS$wealth                  =  18;
    public static final int      HS$ghost_count             =  19;
    public static final int      HS$esp_send_count          =  20;
    public static final int      HS$esp_recv_count          =  21;
    public static final int      HS$requests                =  22;
    public static final int      HS$MAX                     = HS$requests + 1;
    
    
    public static final String[] CLIENT_ERRORS              = { "user_requested_dump", "memory_full", "memory_fault",
            "memory_corrupt", "indirect_address_error", "bad_image_state", "no_room_for_head", "indirect_address_jmp",
            "indirect_address_rts", "heap_overrun", "invalid_comm", "missing_object", "bad_link_attempt",
            "very_bad_links" };
    
    public static final int[] image_base = {
            0,   1,   8,   9,  10,  11,  12,  14, 
            0,   0,  16,   0,  25,  26,   0,   0, 
           28,  29,  30,   0,  31,  32,  34,  35, 
           42,  43,  44,  45,  46,  47,  48,  51, 
           52,  54,   0,  55,  56,  58,  62,   0, 
            0,   0,  64,  66,  80,  82,  83,  85, 
           90,  91,   0,   0,  92,   0,  98,  99, 
          102, 109, 116,   0, 119, 120,   0,   0, 
          123,   0,   0,   0,   0, 124, 137,   0, 
            0,   0, 138, 139, 140,   0,   0,   0, 
          141,   0, 154,   0, 156, 157, 159, 161, 
          163, 164, 165, 166, 167, 168, 181, 182, 
          183, 185, 206, 207,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0, 209, 
            0, 430, 431, 432, 434, 435, 440, 448, 
          452, 453, 455, 456, 457, 458,   0, 459, 
          460,   0,   0, 461,   0,   0, 462,   0, 
          463, 467, 468, 470, 474, 487, 491,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0, 492};
    
    public static final int[] image_x_left = { 0,
            -20, -20, -20, -20, -20, -20, -20, -16, 
            0, -12, -16, -16, -16, -16, -12, -12, 
          -16, -16, -16, -16, -16, -16, -16, -16, 
          -16, -16, -12, -12, -16, -12, -16, -20, 
          -20, -12,   0,   0, -12, -16,   0,   0, 
            0, -12, -16, -12, -16,  12, -20, -20, 
          -20, -16, -20, -16, -16, -20, -16,   0, 
            0, -16, -16, -16, -16, -40,   0, -20, 
          -16, -16, -16, -16, -16, -16, -16, -12, 
          -16, -16, -16, -16, -20, -20, -20, -16, 
          -16, -12, -20, -20, -16, -20, -16, -16, 
          -12, -20, -20, -16, -16, -116, -16, -16, -16, -16, -16, -16, -12, 
          -16, -16, -16, -16, -20, -20, -20, -16, 
          -16, -12, -20, -20, -16, -20, -16, -16, 
          -12, -20, -20, -16, -16, -16, -16, -16, 
          -20, -16, -16, -16, -16, -16, -16, -24, 
          -48, -16, -28, -12, -16, -16, -24, -48, 
          -16, -28, -12, -12, -12, -12, -20, -20, 
          -20, -12, -20,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0, -16, -12,   0, 
          -16,   8, -20, -20,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0, -16, -12, 
            0, -20, -16, -16,   0,   0,  48,  32, 
            0,   0, -16, -12, -16, -16,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0, -16, -12,   0,   0, -16,  28,  28, 
          -16, -16, -16, -12,  -8, -16, -12, -20, 
          -12, -12, -                 0,   0,   0,   0,   0,   0,   0,   0, 
            0, -16, -12,   0,   0, -16,  28,  28, 
          -16, -16, -16, -12,  -8, -16, -12, -20, 
          -12, -12, -20, -12, -12, -12, -12, -12, 
          -16, -12, -12, -16,   0,   0,  24,  28, 
          -64, -64, -64, -64, -64, -64, -64, -64, 
          -64, -64, -64, -64, -64, -64, -64, -64, 
          -64, -64, -64, -64, -64, -64, -64, -64, 
          -64, -64, -64, -64, -64, -64, -64, -64, 
          -16, -16,   0, -20, -16, -16, -16, -20, 
          -16, -64, -64, -64, -64, -64,   0, -64, 
            0, -64, -64, -64, -64, -64, -64, -64, 
          -64, -64, -64, -64, -64, -64, -64, -64, 
            0, -16, -48, -16, -20, -16, -16, -16, 
          -20, -48, -48, -64, 
            0, -16, -48, -16, -20, -16, -16, -16, 
          -20, -48, -48, -48, -48, -48, -48, -16, 
          -64, -64, -64, -64, -64, -64, -64, -64, 
          -64, -64, -64, -64, -64, -64, -64, -64, 
          -64, -64, -64, -64, -64, -64, -64, -64, 
          -64, -64, -64, -64, -64, -64,   0, -64, 
            0,  -8, -64, -64, -64, -64, -64, -64, 
          -64, -64, -64, -64, -64, -64, -64, -64, 
          -12, -16, -16, -16, -16, -16, -16, -16, 
          -16, -64, -16,   0, -20,   0, -16, -16, 
          -20, -64, -64, -64, -64, -64, -64, -64, 
          -64, -64, -64, -64, -64, -64, -64, -64, 
          -64, -64, -16,  -4, -12,  16,   8, -12, 
          -12,  -8, -16, -12,   0,  28,  24,  24, 
            0,  32,  24,  24,  -8,   0, -24,  -4, 
          -12,   8,   8,  16, -12, -12,   8,  -86,  -4, -12,  16,   8, -12, 
          -12,  -8, -16, -12,   0,  28,  24,  24, 
            0,  32,  24,  24,  -8,   0, -24,  -4, 
          -12,   8,   8,  16, -12, -12,   8,  -8, 
            8, -12, -16, -12, -16, -12, -12,   0, 
          -12, -12, -12, -12, -12, -12, -12, -12, 
          -12, -16, -12, -12, -16, -12, -16, -12, 
          -16, -12,   8, -16, -16, -16, -16,  20, 
            0};
    
    public static final int[] image_x_right = {0,
            20,  20,  20,  20,  20,  20,  20,  24, 
            0,  28,  24,  20,  28,  24,  28,  28, 
           28,  28,  24,  24,  24,  24,  28,  24, 
           24,  32,  36,  32,  24,  44,  24,  24, 
           24,  52,  32,  32,  40,  36,  32,  32, 
           32,  28,  24,  28,  24,  12,  20,  20, 
          
           24,  32,  36,  32,  24,  44,  24,  24, 
           24,  52,  32,  32,  40,  36,  32,  32, 
           32,  28,  24,  28,  24,  12,  20,  20, 
           20,  24,  24,  28, -16,  20,  24,   0, 
            0,  20,  20,  20,  24, -40,   0,  24, 
           28,  24,  24,  24,  24,  24,  24,  28, 
           24,  28,  24,  24,  20,  28,  24,  24, 
           24,  32,  20,  20,  24,  20,  24,  24, 
           28,  16,  20,  28,  36,  24,  24,  24, 
           24,  24,  28,  32,  28,  36,  40,  16, 
           12,  56,  28,  60,  36,  40,  16,  12, 
           56,  28,  60,  28,  28,  28,  20,  24, 
           24,  36,  24,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,  24,  32,   0, 
           28,   8,  24,  20,   0,   0,   0,   0, 
            0,   0,   0,   0,        24,  36,  24,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,  24,  32,   0, 
           28,   8,  24,  20,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,  24,  32, 
            0,  24,  24,  24,   0,   0,  48,  32, 
            0,   0,  40,  32, -16,  20,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,  24,  32,   0,   0,  28,  28,  28, 
           24,  20,  24,  24,  32,  28,  28,  24, 
           36,  44,  20,  28,  28,  28,  28,  28, 
           24,  32,  32,  24,   0,   0, -128,  28, 
            8,   8,   8,   8,   8,   8,   8,   8, 
            8,   8,   8,   8,   8,   8,   8,   8, 
            8,   8,   8,   8,   8,   8,   8,   8, 
            8,   8,   8,   8,   8,   8,                   8,   8,   8,   8,   8,   8,   8,   8, 
            8,   8,   8,   8,   8,   8,   8,   8, 
           28,  24,   0,  20,  28,  56, -16,  28, 
           40,   8,   8,   8,   8,   8,   0,   8, 
           32,   8,   8,   8,   8,   8,   8,   8, 
            8,   8,   8,   8,   8,   8,   8,   8, 
            0,  56,  12,  56,  20,  56,  56, -16, 
           28,  12,  12,  12,  12,  12,  12,  28, 
            8,   8,   8,   8,   8,   8,   8,   8, 
            8,   8,   8,   8,   8,   8,   8,   8, 
            8,   8,   8,   8,   8,   8,   8,   8, 
            8,   8,   8,   8,   8,   8,   0,   8, 
           32,  32,   8,   8,   8,   8,   8,   8, 
            8,   8,   8,   8,   8,   8,   8,   8, 
           28,  28,  28,  24,  24,  24,  24,  28, 
           24,   8,  24,   0,  20,   0,  56, -16,  
            8,   8,   8,   8,   8,   8,   8,   8, 
           28,  28,  28,  24,  24,  24,  24,  28, 
           24,   8,  24,   0,  20,   0,  56, -16, 
           28,   8,   8,   8,   8,   8,   8,   8, 
            8,   8,   8,   8,   8,   8,   8,   8, 
            8,   8,  40,  32,  84,  24,   8,  28, 
           24,  32,  28,  28,  16,  28,  24,  24, 
           16,  32,  24,  24,  40,  32, -24,  32, 
           28,  40,  40,  16,  20,  24,  28,  32, 
           64,  44,  28,  24,  28,  56,  40,   0, 
           28,  28,  44,  44,  52,  44,  28,  28, 
           28,  24,  32,  32,  24,  32,  24,  32, 
           24,  28,   8,  32,  32,  24,  36,  20, 
            0};
    
    public static final int[] image_celWidth = { 0,
           -104, -104, -104, -104, -104, -104,   0,   0, 
            -32,  -8,   0,   0,  -8,   0,   0,  -8, 
              0,   0,   0,   0,   0,   0,   0,   0, 
              0,  -8, -16,  -8,   0, -40,   8,   8, 
            264, -16,  -8,  -8, -40, -40,  -8,  -8, 
             -8,   0,   0,  -8,   0, -24,   8,   0, 
              8,  -8,   8,  -8,   0,   8,   0,   0, 
              0,   0,   0,   0,   0,  56,  -8,   8, 
              0,  -8,   0,   0,   0,   0,   0,  -8, 
             -8,   0,   0,   0,   8,   0,   0,   0, 
              0,   0,   8,   8,   0,   8,   8,   8, 
             -8,   0,   8, -16,   0,   0,   0,   0, 
              8,   0, -16,   8,   8, -32, -40,  -8, 
             40, -64,  -8, -56, -32, -40,  -8,  40, 
            -64,  -8, -56,  -8,  -8,  -8,   8,   0, 
              0, -24,   8,   0,   0, -64, -64,   8, 
              8,   8,   0,   0, -40,   0, -48, -48, 
              0, -16,   0,   8,   0,   0, -64, -64, 
              8,   8,   8,   0,   0, -40,   0, -48, 
            -48,   0,   0,   0, -16,   8, -40, -80, 
              0,   0,  -8,  -8, -16,   0,   0,   0, 
              0, -64, -64,   8,   8,   8,   0,   0, 
            -40,   0, -48, -48, -16,   0,   0,  16, 
              0,  -8, -16,   0, -24, -16,  -8,   0, 
            -24, -56,   8, -16,  -8,  -8,  -8,  -8, 
            -16, -40, -24,   0, -48, -16,   8, -40, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
              0,   0,  -8,   8,   0, -64,   0,   0, 
             -8, -104, -104, -104, -104, -104,   0, -104, 
              0, -104, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
            -32, -64,  40, -64,   8, -64, -64,   0, 
              0,  40,  40,  40,  40,  40,  40, -16, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104, -104,   0, -104, 
              0, -24, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
             -8,   0,   0,   0,   0,   0,   0,   0, 
              0, -104,   0,  -8,   8,   0, -64,   0, 
              0, -104, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104, -104,   0, -104, 
              0,   0, -104, -104, -104,   0, -104, -104, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
             -8, -16, -16, -16,  -8, -16, -24, -24, 
            -32,  40,   0,   8,   0, -48,   0,   0, 
           -104, -104, -104, -104, -104, -104, -104, -104, 
           -104, -104, -104, -104, -104,  -8, -72, -72, 
            -40, -48,  -8,   0, -24, -16, -24,  -8, 
            -16, -16, -16,  -8, -16, -24, -24, -16, 
             -8, -16, -32, -16, -64, -64, -32,   0, 
              0, -32, -24, -64, -56,  -8, -48,  -8, 
            -48, -40,  48,  -8,  -8, -16, -40, -16, 
            -64,  -8,  -8,  -8, -16, -40, -24,   0, 
            -48, -16, -48,  -8, -24, -56,   8, -16, 
            -16, -24, -16, -16};
    
    public static final int[] image_y = { 0,
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,  -1,  -1,  -1,  -1,  -1,  -3,  -1, 
           -1,  -1,  -1,  -1,  -1,  -4,  -1,  -1, 
           -4,  -1,  -1,  -3,  -1,  -1,  -1,  -1, 
           -1,  -1,  -1,  -1,   0,   1,  -1,  -1, 
           -1,  -1,  -1,  -1,  -1,  -4,  -1,  -5, 
           -1,  -1,  -1,  -1,  -1,  -5,  -1,   0, 
            0,  -1,  -1,  -1,  -1, -72,   0,  -3, 
           -4,  -1,  -1,  -1,  -1,  -1,  -1,  -1, 
           -1,  -1,  -1,  -1,  -1, -10,  -1,  -3, 
           -3,  -1,  -1,  -1,  -1,  -1,  -1,  -1, 
           -1,  -1,  -1,  -1,   1,  -1,  -1,  -1, 
1,  -1,  -1,  -1,  -1,  -1,  -1, 
           -1,  -1,  -1,  -1,   1,  -1,  -1,  -1, 
           -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1, 
           -1,  -1,  -1,  -5,  -1,  -1,  -1,  -1, 
           -1,  -1,  -5,  -1,  -1,  -1,  -4,  -1, 
           -1,  -1,  -1,   0,   0,  12,  12,  12, 
           12,  12,  12,   0,   0,  -1, -10,   0, 
           -1,   3,  -4,  -4,   0,   0,  12,  12, 
           12,  12,  12,  12,   0,   0,  -1, -10, 
            0,  -1,  -1,  -1,   0,   0,  -2,  -2, 
            0,   0,  -8,  -9,   1,  -1,   0,   0, 
            0,  12,  12,  12,  12,  12,  12,   0, 
            0,  -1, -10,   0,   0,  -1,   1,   1, 
           -1, -34, -46,  -1,  -1,  -1,  -1,  -1, 
           -1,  -1,  -1,  -1,  -1,  -1,  -1,   1, 
           -1,  -1,  -1,  -1,   0,   0,  28,   1, 
1,   1,   1, 
           -1, -34, -46,  -1,  -1,  -1,  -1,  -1, 
           -1,  -1,  -1,  -1,  -1,  -1,  -1,   1, 
           -1,  -1,  -1,  -1,   0,   0,  28,   1, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
           -1,  -3,   0,  -5,  -1,  -1,  -1, -10, 
           -8,   0,   0,   0,   0,   0,   0,   0, 
           -1,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,  -1,  -1,  -1,  -5,  -1,  -1,  -1, 
          -10,  -1,  -1,  -1,  -1,  -1,  -1,  -1, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,                 -10,  -1,  -1,  -1,  -1,  -1,  -1,  -1, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
           -1,  -1,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
           -1,  -1,  -1,  -1,  -1,  -1,  -4,  -1, 
           -1,   0,  -3,   0,  -5,   0,  -1,  -1, 
          -10,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,   0,   0,   0,   0,   0,   0, 
            0,   0,  -1,  -1,   1,  -1,  -1,  -1, 
           -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1, 
           -1,  -1,  -1,  -1,  -1,  -1,  -1,  -4, 
           -1,  -1,  -1,  -1,  -1,  -1,  -4,  -1, 
           -1,  -1,  -1,  -1,  -1,  -1,  -1,   0, 
           -1,  -1,                  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -4, 
           -1,  -1,  -1,  -1,  -1,  -1,  -4,  -1, 
           -1,  -1,  -1,  -1,  -1,  -1,  -1,   0, 
           -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1, 
            1,  -1,  -1,  -1,  -1,  -1,  -1, -10, 
           -1,  -1,  -1,  -1,  -1,  -1,  -1,  -5, 
            0};
}
