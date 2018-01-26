package org.made.neohabitat;

import org.elkoserver.server.context.BasicObject;
import org.elkoserver.server.context.Item;
import org.elkoserver.server.context.Mod;
import org.elkoserver.server.context.ObjectCompletionWatcher;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Random;

import org.elkoserver.foundation.json.JSONMethod;
import org.elkoserver.foundation.json.OptBoolean;
import org.elkoserver.foundation.json.OptInteger;
import org.elkoserver.foundation.json.OptString;
import org.elkoserver.server.context.User;
import org.elkoserver.util.trace.Trace;
import org.made.neohabitat.mods.Avatar;
import org.made.neohabitat.mods.Compass;
import org.made.neohabitat.mods.Flashlight;
import org.made.neohabitat.mods.Pawn_machine;
import org.made.neohabitat.mods.Region;
import org.made.neohabitat.mods.Tokens;
import org.elkoserver.json.EncodeControl;
import org.elkoserver.json.JSONLiteral;

import java.util.Formatter;

// TODO CHIP? Should we assureSameContext (and other elko patterns) assuming ill behaving clients? If so, how and when?

/**
 * This is the master superclass of the Habitat Elko project.
 * 
 * All of the game-specific behavior is either in this class or in one of it's
 * descendants. It implements defaults for all the required verbs, the common
 * state that every habitat object has, and lots of generic behavior.
 * 
 * The Elko Open Source project provides a replacement for all of the critical
 * services provided by Q-Link/AOL. It also provides all of the connection
 * management, object database, and message dispatching services that were part
 * of Habitat's server core (non-game logic).
 * 
 * ALL of the original source that is ported here was written in a 1980's flavor
 * of Stratus PL1. PL1 was all about globals, pass by reference, and no support
 * for classes. 30 years ago, Chip Morningstar wrote the bulk of the Habitat
 * game-logic code, simulating structures, classes, and a form of class
 * inheritance by concatenating include files and careful management of
 * procedure references.
 * 
 * The game-logic port of PL1 Habitat to Elko Habitat [Java] focuses on clarity
 * of checking that the translated code is correct and visibly similar to the
 * original code.
 * 
 * This means that some of the coding practices evolved over the last three
 * decades have been set aside. For example - naming conventions about
 * mixed-caps and underscores have been "rolled back" to 1988 - at least in
 * terms of the behavior code. Any 100% new Java code may use modern naming
 * conventions are used (For example, the Habitat mod names are all start with a
 * capital letter.)
 * 
 * The non-static constant globals present a different problem and have been
 * replaced with public state that is accessed by getter functions. So the PL1
 * contents array has been replaced with contents(). Likewise bit-flags are now
 * boolean arrays in memory and they are [un]packed for storage/transmission as
 * needed. And simple dereference functions such as avatar(), container(),
 * current_region(), and position() are provided where globals once were.
 * 
 * @author randy
 *
 */
public abstract class HabitatMod extends Mod implements HabitatVerbs, ObjectCompletionWatcher {

    /* Instance Variables shared by all Habitat objects */

    // public int avatarslot = 0; Obsolete, the connection is clearly
    // represented by the User
    // public int obj_id = 0; Replaced with item.ref() getter
    /**
     * 0-255. Ephemeral Numeric Object ID: The client's world model of region
     * contents, 0 = THE_REGION.
     */
    public int     noid        = UNASSIGNED_NOID;
    /**
     * 0-255 Each Habitat Class has a 0-based table mapping to a global 0-255
     * space of graphics.
     */
    public int     style       = 0;
    /** The horizontal position, if the container is THE_REGION */
    public int     x           = 0;
    /**
     * 0-127 If in THE_REGION, the vertical position (+128 if foreground),
     * otherwise the offset within the container.
     */
    public int     y           = 0;
    /** Each graphic resource has multiple views (orientations) */
    public int     orientation;
    // public int position = 0; position always == this.y, so replaced with a
    // getter.
    public int     gr_state    = 0;
    // public int container = 0; containership is managed by Elko - use it's
    // understanding to access the objects
    public int     gr_width    = 0;
    /** Persistent store if the object is restricted (can't leave region or enter a non-restricted container) or is itself a restricted container */
    public boolean restricted  = false;
    
    /** local storage of the last container that had a restricted bit, only used this object is restricted itself - used to put the object back in place.*/
    public Container lastRestrictedContainer = null;
    
    public boolean gen_flags[] = new boolean[33];

    /* Provides an initialized random number generator for any derived classes. */
    protected static final Random rand = new Random();

    /* This item fits on the heap in the C64 clients memory. */
    public  boolean fits = false;
    
    /**
     * Replaces original global 'position'
     * 
     * @return this.y which is a synonym for position within a container.
     */
    public int position() {
        return y;
    }

    /**
     * Replaces original global 'obj_id'
     * 
     * NOTE: Changes it's type from originally numeric to string.
     * 
     * @return The unique object database identity for the object
     */
    public String obj_id() {
        return this.object().ref();
    }

    /**
     * Replaces globals avatar/avatarptr
     * 
     * @param user
     *            Who you want to get the Avatar Mod for.
     * @return Gets the avatar mod for a user
     */
    public Avatar avatar(User user) {
        return (Avatar) (user.getMod(Avatar.class));
    }

    /**
     * Replaces global current_region
     * 
     * @return The Region mod attached to the current Elko context.
     */
    public Region current_region() {
        return (Region) context().getMod(Region.class);
    }

    /**
     * Replaces global container
     * 
     * @param obj
     *            The habitat mod that wants to find it's container.
     * @return The container mod for the item containing the obj.
     */
    public Container container(HabitatMod obj) {
        return (Container) obj.object().container().getMod(Container.class);
    }

    /**
     * Replaces global container (altenate interface)
     * 
     * @return The container mod for the item containing 'this'.
     */
    public Container container() {
        return container(this);
    }

    /**
     * Can this be painted with a Changomatic?    
     * @return
     */
    public boolean changeable() {
        return false;
    }

    
    /**
     * Constructor.
     * 
     * This is an abstract class, and the constructor is only ever called by the
     * actual habitat objects
     * 
     * @param style
     *            style offset to choose the presentation image default:0
     * @param x
     *            horizontal screen position default: 0
     * @param y
     *            vertical screen position/z-depth default: 0
     * @param orientation
     *            graphic image orientation default: 0
     * @param gr_state
     *            animation/graphic state default:0
     */

    public HabitatMod(int style, int x, int y, int orientation, int gr_state, boolean restricted) {
        this.style              = style;
        this.x                  = x;
        this.y                  = y;
        this.orientation        = orientation;
        this.gr_state           = gr_state;
        gen_flags[RESTRICTED]   = restricted;
    }

    public HabitatMod(OptInteger style, OptInteger x, OptInteger y, OptInteger orientation, OptInteger gr_state, OptBoolean restricted) {
        this(style.value(0), x.value(0), y.value(0), orientation.value(0), gr_state.value(0), restricted.value(false));
    }

    public void objectIsComplete() {
        HabitatMod container = container();
        if (!(container.HabitatClass() == CLASS_AVATAR && ((Avatar) container).amAGhost)) {
            Region.addToNoids(this);
            if (container.opaque_container()) {
                note_instance_creation(this);
            } else {
                note_object_creation(this);
            }
        }
    }

    public JSONLiteral encodeCommon(JSONLiteral result) {
        if (result.control().toClient()) {
            result.addParameter("noid", noid);
        }
        result.addParameter("style", style);
        result.addParameter("x", x);
        result.addParameter("y", y);
        result.addParameter("orientation", orientation);
        result.addParameter("gr_state", gr_state);
        if (gen_flags[RESTRICTED]) {
            result.addParameter("restricted", true);
        }
        /*
         * Do not do result.finsh() here. Each Habitat Class does the final
         * assembly.
         */
        return result;
    }

    /**
     * Dump a trace message that an illegal request was received into the log.
     * 
     * @param from
     *            User representing the connection making the request.
     */
    public void illegal(User from) {
        illegal(from, "unspecified");
    }

    /**
     * Dump a trace message that an illegal request was received into the log.
     * 
     * @param from
     *            User representing the connection making the request.
     * @param request
     *            The string describing the rejected request.
     */
    public void illegal(User from, String request) {
        send_reply_error(from);
        trace_msg("Illegal request:'" + request + "' from: " + from.ref());
        object_say(from, noid, "Illegal command request. This has been logged.");
    }

    /**
     * Verb (Debug): Test rigging for Elko Habitat developers to write trace
     * messages into the log without actually doing anything
     * 
     * @param from
     *            User representing the connection making the request.
     */
    @JSONMethod
    public void TEST(User from) {
        trace_msg("This.getClass().getName(): " + this.getClass().getName());
    }

    /**
     * Verb (Generic): Get HELP for this.
     * 
     * Unlike most verbs, HELP has a useful default implementation that applies
     * to all classes that don't choose to override it.
     * 
     * @param from
     *            User representing the connection making the request.
     */
    @JSONMethod
    public void HELP(User from) {
        generic_HELP(from);
    }

    /**
     * Verb (Illegal): This shouldn't get here. Log it.
     * 
     * @param from
     *            User representing the connection making the request.
     */
    @JSONMethod ({"text"})
    public void ASK(User from, OptString text) {
        illegal(from, this.HabitatModName() + ".ASK");
    }

    /**
     * Verb (Illegal): This shouldn't get here. Log it.
     * 
     * @param from
     *            User representing the connection making the request.
     */
    @JSONMethod
    public void DO(User from) {
        illegal(from, this.HabitatModName() + ".DO");
    }

    /**
     * Verb (Illegal): This shouldn't get here. Log it.
     * 
     * @param from
     *            User representing the connection making the request.
     */
    @JSONMethod
    public void RDO(User from) {
        illegal(from, this.HabitatModName() + ".RDO");
    }

    /**
     * Verb (Generic): Pick this item up.
     * 
     * @param from
     *            User representing the connection making the request.
     */
    @JSONMethod
    public void GET(User from) {
        illegal(from, this.HabitatModName() + ".GET");
    }

    /**
     * Verb (Generic): Put this item into some container or on the ground.
     * 
     * @param from
     *            User representing the connection making the request.
     * @param containerNoid
     *            The Habitat Noid for the target container THE_REGION is
     *            default.
     * @param x
     *            If THE_REGION is the new container, the horizontal position.
     *            Otherwise ignored.
     * @param y
     *            If THE_REGION: the vertical position, otherwise the target
     *            container slot (e.g. HANDS/HEAD or other.)
     * @param orientation
     *            The new orientation for the object being PUT.
     */
    @JSONMethod({ "containerNoid", "x", "y", "orientation" })
    public void PUT(User from, OptInteger containerNoid, OptInteger x, OptInteger y, OptInteger orientation) {
        illegal(from, this.getClass().getName() + ".PUT");
    }

    /**
     * Verb (Generic): Throw this across the Region
     * 
     * @param from
     *            User representing the connection making the request.
     * @param x
     *            Destination horizontal position
     * @param y
     *            Destination vertical position (lower 7 bits)
     */
    @JSONMethod({ "target", "x", "y" })
    public void THROW(User from, int target, int x, int y) {
        illegal(from, this.getClass().getName() + ".THROW");
    }

    /**
     * Almost all efforts to GET a Habitat object go through this code. It has
     * lots of special cases.
     * 
     * Various ways GET can fail: the Avatar is already holding something OR the
     * object is not getable OR the object is not accessible OR the object is an
     * open container OR the object is in glue or some other permanent container
     * OR the object is in another Avatar's pockets OR the object is in another
     * Avatar's hands and can't be grabbed OR the Avatar holding the object is
     * offline OR the object is in a display case and belongs to the case's
     * owner OR there's just not enough room here to hold the object!
     * 
     * @param from
     *            User representing the connection making the request.
     */
    public void generic_GET(User from) {

        Container cont = container();
        int selfClass = this.HabitatClass();
        int contClass = cont.HabitatClass();

        final int FROM_POCKET = 1;
        final int FROM_GROUND = 0;

        if (!empty_handed(avatar(from)) || !getable(this) || (!accessable(this, from) && selfClass != CLASS_GAME_PIECE)
                || contClass == CLASS_BUREAUCRAT || contClass == CLASS_VENDO_FRONT || contClass == CLASS_VENDO_INSIDE
                || contClass == CLASS_GLUE) {
            send_reply_error(from);
            return;
        }

        if ((selfClass == CLASS_BOX || selfClass == CLASS_BAG) && ((Openable) this).open_flags[OPEN_BIT]) {
            send_reply_error(from);
            return;
        }
        if ((position() != HANDS || !grabable(this)) && (contClass == CLASS_AVATAR && cont.noid != avatar(from).noid)) {
            send_reply_error(from);
            return;
        }

        /*
         * OBSOLETE CODE: An Elko User == Connection, so there is no Turned to
         * Stone state. And since a Elko Habitat Avatar is attached 1:1 to a
         * Elko User, this code can never be true in this server.
         * 
         * if (contClass == CLASS_AVATAR &&
         * ^UserList(cont.avatarslot)->u.online) { send_reply_error(from);
         * return; }
         */

//      FRF: The old model of limited access to containers was display-case only.
//      Limiting access is now changed to be for all containers in a turf or residence.
//      
//      This is the old code that was display-case only:        
//      String avatar_userid = from.ref();
//      if (contClass == CLASS_DISPLAY_CASE) {
//          if (/* TODO DisplayCase dcont.locked(self.position+1)&dcont.owner */ "" != avatar_userid) {
//              object_say(from, cont.noid, "You are not the shopkeeper.  You cannot pick this item up.");
//              send_reply_error(from);
//              return;
//          }
//      }
        
        // NEW FRF: Don't let visitors to turfs manipulate immobile containers/contents.
        if (contClass != CLASS_AVATAR && contClass != CLASS_REGION && !cont.meetsOwnershipRestrictions(from)) {
            object_say(from, cont.noid, "This is somone else's property.  You cannot pick this item up.");
            send_reply_error(from);
            return;
        }
        

        /*
         * All the preemptive tests have passed, we can really try to pick this
         * item up!
         */

        /* Where object is gotten from determines the choreography required */
        int how;

        if (cont.noid == avatar(from).noid)
            how = FROM_POCKET;
        else
            how = FROM_GROUND;

        /*
         * int original_position = position() + 1; Original dead code that is
         * never referenced. FRF
         */
        boolean previousContainerWasOpaque = container_is_opaque(container(), y);

        if (!change_containers(this, (Container) avatar(from), HANDS, true)) {
            send_reply_error(from);
            return;
        }
        
        if (this.gen_flags[RESTRICTED] && cont.gen_flags[RESTRICTED]) {
            lastRestrictedContainer = (Container) cont;     // Save so we can put it back where it was.
        }
        
        /*
         * If getting a switched on flashlight from an opaque container, turn up
         * the lights.
         */
        if (selfClass == CLASS_FLASHLIGHT) {
            if (((Flashlight) this).on == TRUE) {
                if (previousContainerWasOpaque) {
                    current_region().lighting += 1;
                    send_broadcast_msg(THE_REGION, "CHANGELIGHT_$", "adjustment", +1);
                }
            }
        }

        /* If Tome Of Wealth And Fame, Notify Sysop */
        if (this.object().ref() == "The Tome of Wealth And Fame")
            message_to_god(this, avatar(from), "Tome Recovered!");

        /* If getting a compass, match its orientation to the current region */
        if (selfClass == CLASS_COMPASS) {
            gr_state = current_region().orientation;
            send_fiddle_msg(THE_REGION, noid, C64_GR_STATE_OFFSET, current_region().orientation);            
        }
        send_reply_success(from); // Yes, your GET request succeeded.
        send_neighbor_msg(from, avatar(from).noid, "GET$", "target", noid, "how", how); // Animate the picking up for other folks here.

        // TODO THIS IS WRONG? Deal with at change_containers
        /*
         * if (Avatar.getConnectionType() == CONNECTION_JSON &&
         * container_is_opaque(cont, y)) { context().sendToNeighbors(from,
         * Msg.msgDelete(this.object())); }
         */
    }

    /**
     * Simple 0-parameter PUT version provided to allow for JSON interface
     * testing. Drops the item at the avatar's feet.
     * 
     * @param from
     *            User representing the connection making the request.
     * @returns whether the PUT succeded
     */
    public boolean generic_PUT(User from) {
        return generic_PUT(from, (Container) current_region(), avatar(from).x, avatar(from).y, avatar(from).orientation);
    }

    /**
     * Put this into a new container specified by noid.
     * 
     * @param from
     *            User representing the connection making the request.
     * @param containerNoid
     *            The noid of the new container for this.
     * @param x
     *            The new horizontal position of the object (if the container is
     *            THE_REGION)
     * @param y
     *            The new vertical position in THE_REGION or slot number in the
     *            new container.
     * @param orientation
     *            The new orientation for this once transfered.
     * @returns whether the PUT succeded
     */
    // TODO @deprecate containerNoid/ObjList?
    public boolean generic_PUT(User from, int containerNoid, int x, int y, int orientation) {
        Region region = current_region();
        Container target = region;
        if (containerNoid != THE_REGION) {
            if (region.noids[containerNoid] instanceof Container) {
                target = (Container) region.noids[containerNoid];
            } else {
                trace_msg("Class " + region.noids[containerNoid].object().ref() + " is not a container.");
                return false;
            }
        }
        return generic_PUT(from, target, x, y, orientation);
    }
    
    public void putMeBack(User from, boolean copyMe) {
        Container cont = this.lastRestrictedContainer;
        if (cont != null) {
            int slot = -1;
            for (int i = 0; i < cont.capacity(); i++) {
                if (cont.contents(i) == null) {
                    slot = i;
                    break;
                }
            }
            if (slot >= 0 && change_containers(this, cont, slot, true)) {
                JSONLiteral msg = copyMe ? new_broadcast_msg(avatar(from).noid, "PUT$") :
                    new_neighbor_msg( avatar(from).noid, "PUT$");
                msg.addParameter("obj",     this.noid);
                msg.addParameter("cont",    cont.noid);
                msg.addParameter("x",       0);
                msg.addParameter("y",       slot);
                msg.addParameter("how",     0);
                msg.addParameter("orient",  0);
                msg.finish();
                if (copyMe) {
                    context().send(msg);
                } else {
                    context().sendToNeighbors(from, msg);
                }
            }
        }
    }
    
    /**
     * Most attempt to PUT an item go through this code. There are lots of
     * special cases.
     * 
     * Various ways PUT can fail: the container noid specified by the C64 is
     * invalid OR it's trying to put down a magic lamp in the genie state OR the
     * Avatar is not holding the object OR the target location is not available
     * (already occupied) OR it's putting a restricted object into a
     * non-restricted container OR it's trying to put a flag into a container
     * (not allowed) OR the call to change_containers fails because there is not
     * enough room (this should never happen, since the object is already out,
     * but we check just in case)
     * 
     * @param from
     *            User representing the connection making the request.
     * @param cont
     *            The noid of the new container for this.
     * @param pos_x
     *            The new horizontal position of the object (if the container is
     *            THE_REGION)
     * @param pos_y
     *            The new vertical position in THE_REGION or slot number in the
     *            new container.
     * @param obj_orient
     *            The new orientation for this once transfered.
     */
    public boolean generic_PUT(User from, Container cont, int pos_x, int pos_y, int obj_orient) {
        
        
        final int TO_AVATAR = 1;
        final int TO_GROUND = 0;

        Item item               = (Item) this.object();
        int selfClass           = this.HabitatClass();
        int contClass           = cont.HabitatClass();
        HabitatMod oldContainer = this.container();
        int oldY                = y;
        
        if (gen_flags[RESTRICTED] && lastRestrictedContainer != null && lastRestrictedContainer != cont) {
            send_reply_error(from);         // Nope. Let's put it back instead.
            putMeBack(from, true);
            return false;
        }
        
        // NEW FRF: Don't let visitors to turfs manipulate immobile containers/contents.
        if (contClass != CLASS_AVATAR && contClass != CLASS_REGION && !cont.meetsOwnershipRestrictions(from)) {
            object_say(from, cont.noid, "This is somone else's property.  You cannot put that there.");         
            send_reply_error(from);  // No leaving presents in turf furniture.
            return false;

        }


        if (this.container() == null) {
            send_reply_error(from);
            return false;
        }
        if (!holding(avatar(from), this)) {
            send_reply_error(from);
            return false;
        }

        // boolean put_success = false; /* TODO FIX THIS ORIGINAL GLOBAL */
        /*
         * Check to see of the object being PUT (this) is a container and it's
         * open
         */

        if (this instanceof Openable && ((Openable) this).open_flags[OPEN_BIT]) {
            trace_msg("PUT WHILE OPEN: " + from.ref() + " attempted to put " + item.ref() + " into containter "
                    + cont.object().ref());
            send_reply_error(from);
            return false;
        }

        if (cont.noid != THE_REGION) {
            if (contClass != CLASS_AVATAR && cont instanceof Openable && !((Openable) cont).open_flags[OPEN_BIT]) {
                send_reply_error(from); // Tried to put into a CLOSED or OWNED container
                return false;
            }
            pos_y = -1;
            int token_at = -1;
            int j = cont.capacity();
            if (contClass == CLASS_AVATAR) {
                j = j - 3;
            }
            for (int i = 0; i < j; i++) {
                HabitatMod obj = cont.contents(i);
                if (obj == null) {
                    if (pos_y == -1)
                        pos_y = i;
                } else {
                    if (obj.HabitatClass() == CLASS_TOKENS) {
                        token_at = i;
                    }
                }
            }
            if (selfClass == CLASS_TOKENS && token_at != -1) {
                Tokens inHand      = (Tokens) this;
                Tokens inContainer = (Tokens) cont.contents(token_at); 
                int    newDenom  = inHand.tget() + inContainer.tget();
                if (newDenom > 65535)  {
                    send_reply_error(from);
                    return false;
                }
                inContainer.tset(newDenom);
                inContainer.gen_flags[MODIFIED] = true;
                checkpoint_object(inContainer);
                send_fiddle_msg(THE_REGION, inContainer.noid, C64_TOKEN_DENOM_OFFSET, new int []{newDenom % 256, newDenom/256});
                send_goaway_msg(inHand.noid);
                inHand.destroy_object(inHand);
                send_reply_error(from);         // Well, a merge isn't really a failure, but the client doesn't understand otherwise;               
                return false;
            }
            if (pos_y == -1) {
                if (selfClass != CLASS_PAPER) {
                    send_reply_error(from);
                    return false;
                }
                send_reply_error(from);
                // put_success = true;
                return false;
            }
        }
        if (!available(cont, pos_x, pos_y)) {
            object_say(from, cont.noid, "The container is full.");
            send_reply_error(from);
            return false;
        }
        if (this.gen_flags[RESTRICTED] && !cont.gen_flags[RESTRICTED] && cont.noid != THE_REGION) {
            object_say(from, cont.noid, "You can't put that in there.");
            send_reply_error(from);
            return false;
        }
        if (cont.gen_flags[RESTRICTED] && !this.gen_flags[RESTRICTED])  {
            object_say(from, cont.noid, "You can't put that in there.");
            send_reply_error(from);
            return false;
        }
        if (selfClass == CLASS_MAGIC_LAMP && this.gr_state == MAGIC_LAMP_GENIE) {
            object_say(from, noid, "You can't put down a Genie!");
            send_reply_error(from);
            return false;
        }
        if (selfClass == CLASS_FLAG && cont.noid != THE_REGION) {
            send_reply_error(from);
            return false;
        }
        if (cont.noid == THE_REGION && pos_y < 128) {
            send_reply_error(from);
            return false;
        }
        if (cont.noid == THE_REGION && (pos_x < 8 || pos_x > 152)) {
            send_reply_error(from);
            return false;
        }

        /* Preemptive tests complete! We're ready to change containers! */
        boolean previousContainerWasOpaque = container_is_opaque(container(), y);

        if (!change_containers(this, cont, pos_y, false)) {
            send_reply_error(from);
            return false;
        }

        /* Now for the side effects... */

        boolean going_away_flag = false;

        /* If putting down blank paper, it might disappear. Check. */

        /*
         * TODO CLASS_PAPER if (selfClass == CLASS_PAPER) going_away_flag =
         * ((Writable) this).text_id == NULL;
         */

        /* If putting to the region, set the (x, y) coordinates */
        if (cont.noid == THE_REGION) {
            if (selfClass == CLASS_GAME_PIECE) {
                this.orientation &= ~FOREGROUND_BIT;
                send_broadcast_msg(THE_REGION, "PLAY_$", "sfx_number", 128 + 0, "from_noid", noid);
            }
            this.x = pos_x;
            this.y = pos_y;
            if (obj_orient == 1)
                this.orientation |= FACING_BIT;
            else
                this.orientation &= ~FACING_BIT;
        }
        
//      FRF: The CLASS_DISPLAY_CASE original implementation has been obsoleted with a turf/resident based security model.
//      The commented code below is here for reference..
//      
//      /* If putting into a display case, adjust the locked bit */
//      if (contClass == CLASS_DISPLAY_CASE && !going_away_flag) {
//          /*
//           * TODO DisplayCase DisplayCase case = (DisplayCase) this;
//           * case.locked[this.position() + 1] = (from.name() == case.owner);
//           * case.gen_flags[MODIFIED] = true;
//           */
//      }

        /*
         * If the object is a switched on flashlight and is being put into an
         * opaque container, turn down the lights.
         */
        if (selfClass == CLASS_FLASHLIGHT) {
            if (((Flashlight) this).on == TRUE) {
                if (container_is_opaque(cont, y)) {
                    current_region().lighting -= 1;
                    send_broadcast_msg(THE_REGION, "CHANGELIGHT_$", "adjustment", -1);
                }
            }
        }

        /* If the object is a head, set its gr_state to the dormant mode */
        if (selfClass == CLASS_HEAD && cont.noid != avatar(from).noid)
            gr_state = HEAD_GROUND_STATE;

        /* Where an object is put determines the choreography required */
        int how = TO_GROUND;
        if (cont.noid == avatar(from).noid)
            how = TO_AVATAR;

        /* Inform the world! */
        gen_flags[MODIFIED] = true;
        checkpoint_object(this);
        // put_success = true;

        if (Avatar.getConnectionType() == CONNECTION_JSON) {
            if (container_is_opaque(oldContainer, oldY) && !container_is_opaque(cont, y)) {
                item.sendObjectDescription(context().neighbors(from), context());
            }
        }

        JSONLiteral msg = new_neighbor_msg(avatar(from).noid, "PUT$");
        msg.addParameter("obj", this.noid);
        msg.addParameter("cont", cont.noid);
        msg.addParameter("x", this.x);
        msg.addParameter("y", this.y);
        msg.addParameter("how", how);
        msg.addParameter("orient", this.orientation);
        msg.finish();
        context().sendToNeighbors(from, msg);

/* TODO Opaque container handling        
        if (Avatar.getConnectionType() == CONNECTION_JSON) {
                if (!container_is_opaque(oldContainer, oldY) && container_is_opaque(cont, y)) {
                        context().sendToNeighbors(from, Msg.msgDelete(this.object()));
                }
        }
 */       
        send_reply_msg(from, noid, "err", TRUE, "pos", this.y);

        /* If putting into a pawn machine, announce the value of the object */
        if (contClass == CLASS_PAWN_MACHINE)
            object_say(from, cont.noid, "Item value: $" + Pawn_machine.pawn_values[this.HabitatClass()]);

        return true;
    }

    /**
     * Throw this across the room, onto some kind of surface, by noid.
     * 
     * @param from
     *            User representing the connection making the request.
     * @param target
     *            The noid of the new container for this.
     * @param target_x
     *            The new horizontal position for this.
     * @param target_y
     *            The new vertical position for this.
     * @returns whether the THROW succeded
     */
    // TODO @deprecate target/ObjList?
    public boolean generic_THROW(User from, int target, int target_x, int target_y) {
        return generic_THROW(from, current_region().noids[target], target_x, target_y);
    }

    /**
     * Throw this across the room, onto some kind of surface.
     *
     * Various ways THROW can fail: the target noid specified by the C64 is
     * invalid OR it's trying to throw a magic lamp in the genie state OR the
     * Avatar is not holding the object OR the target class specified by the C64
     * is not allowed OR the call to change_containers fails because there is
     * not enough room (this should never happen, since the object is already
     * out, but we check just in case)
     * 
     * @param from
     *            User representing the connection making the request.
     * @param target
     *            The new container
     * @param target_x
     *            The new horizontal position for this.
     * @param target_y
     *            The new vertical position for this.
     * @returns whether the THROW succeded
     */
    public boolean generic_THROW(User from, HabitatMod target, int target_x, int target_y) {

        int new_x = target_x;
        int new_y = target_y;
        int selfClass = this.HabitatClass();
        int targetClass = target.HabitatClass();
        HabitatMod oldContainer = this.container();
        Avatar avatar = (Avatar) avatar(from);
        int oldY = this.y;
        Item item = (Item) this.object();

        /*
         * DEAD CODE if (target == null) { r_msg_4(from, target.noid, this.x,
         * this.y, FALSE); return; }
         */

        if (!holding(avatar, this)) {
            send_throw_reply(from, noid, target.noid, this.x, this.y, FALSE);
            return false;
        }

        if (selfClass == CLASS_MAGIC_LAMP && this.gr_state == MAGIC_LAMP_GENIE) {
            object_say(from, noid, "You can''t throw a Genie!");
            send_throw_reply(from, noid, target.noid, this.x, this.y, FALSE);
            return false;
        }
        /* If target isn't open ground, object doesn't move */
        if (targetClass != CLASS_STREET && targetClass != CLASS_GROUND) {
            if (targetClass != CLASS_FLAT && targetClass != CLASS_TRAPEZOID && targetClass != CLASS_SUPER_TRAPEZOID) {
                send_throw_reply(from, noid, target.noid, this.x, this.y, FALSE);
                return false;
            }
            if (((Walkable) target).flat_type != GROUND_FLAT) {
                send_throw_reply(from, noid, target.noid, this.x, this.y, FALSE);
                return false;
            }
        }

        if (target_x > 152 || target_x < 8) {
            send_throw_reply(from, noid, target.noid, this.x, this.y, FALSE);
            return false;
        }

        /* Hook for collision detection */
        /*
         * call check_path(target_id, target_x, target_y, new_x, new_y, dummy);
         */ // ORIGINAL NEVER IMPLEMENTED

        /* This check says, simply, "did it go where it was aimed?" */
        if (new_x != target_x | new_y != target_y) { // This is dead code TODO
            // Review to Remove
            send_throw_reply(from, noid, target.noid, this.x, this.y, FALSE);
            return false;
        }
        /*
         * Preflight complete! Lets throw the item into the region at the
         * supplied coordinates
         */

        if (!change_containers(this, current_region(), 0, false)) {
            trace_msg("*ERR* change_containers fails: generic_THROW");
            send_reply_msg(from, noid, "target", target.noid, "x", this.x, "y", this.y, "err", FALSE);
            return false;
        }

        /* Clamp y-coord at region depth */
        new_y = clear_bit(new_y, 8);

        if (new_y > current_region().depth)
            new_y = current_region().depth;

        if (selfClass != CLASS_GAME_PIECE)
            new_y = new_y | FOREGROUND_BIT;
        else
            send_broadcast_msg(THE_REGION, "PLAY_$", "sfx_number", 128 + 0, "from_noid", noid);

        /* Put the object there */

        this.x = new_x;
        this.y = new_y;
        this.orientation = clear_bit(this.orientation, 1);

        /* If it's a head, set its gr_state to the ground mode */
        if (selfClass == CLASS_HEAD)
            this.gr_state = HEAD_GROUND_STATE;

        this.gen_flags[MODIFIED] = true;
        checkpoint_object(this);

        /* Tell all the world */
        if (Avatar.getConnectionType() == CONNECTION_JSON) {
            if (container_is_opaque(oldContainer, oldY) && !container_is_opaque(target, y)) {
                item.sendObjectDescription(context().neighbors(from), context());
            }
        }

        send_neighbor_msg(from, avatar.noid, "THROW$", "obj", noid, "x", new_x, "y", new_y, "hit", TRUE);

/* TODO Opaque container handling        

        if (Avatar.getConnectionType() == CONNECTION_JSON) {
                if (!container_is_opaque(oldContainer, oldY) && container_is_opaque(target, y)) {
                        context().sendToNeighbors(from, Msg.msgDelete(this.object()));
                }
        }
 */

        send_throw_reply(from, noid, target.noid, new_x, new_y, TRUE);

        /* Notes that this throw has been successful. */
        return true;
    }
    
    /**
     * Verb (Specific): TODO Ask of the Oracle!
     *
     * @param from
     *            User representing the connection making the request.
     * @param text
     *            The string to ask!
     */
    public void generic_ASK(User from, OptString text) {
        String question = text.value("");
        Avatar avatar   = avatar(from);
        if (question.toLowerCase().indexOf("to:") == 0) {
            object_say(from, "I don't do ESP.  Point somewhere else.");
        } else {
            if (question.length() < 4)
                question = " " + question + " ";
            object_say(from, avatar.noid, question);
            message_to_god(this, avatar, question);
            if (this.HabitatClass() == CLASS_FOUNTAIN) {
                object_say(from, noid, "Someday, I'll see what I can do.");
                if (question.toLowerCase().equals("willy willy nilly billy")) {
                    object_say(from, noid, "That IS the correct phrase.");
                }
                if (avatar.curse_type > 0) {
                    object_say(from, noid, "By the way, to remove the curse you must give it to someone else." );
                }
            }
        }
    }
    

    private void send_throw_reply(User from, int noid, int target, int x, int y, int err) {
        send_reply_msg(from, noid, "target", target, "x", x, "y", y, "err", err);
    }

    /**
     * Most of the Habitat classes only need simple strings for their HELP
     * messages, so this generic implementation provides that. If no override is
     * specified, this is is the HELP message handler.
     * 
     * @param from
     *            User representing the connection making the request.
     */
    public void generic_HELP(User from) {
        final String help_messages[] = { "i", /* 0 -- region */
                "i", /* 1 -- avatar */
                "i", /* 2 -- amulet */
                "-", /* 3 */
                "Atm: DO displays account balance.  GET withdraws tokens.  PUT deposits tokens into your account.", /*
                 * 4
                 * --
                 * atm
                 */
                "i", /* 5 -- game piece */
                "i", /* 6 -- bag */
                "Recommended for ages 3 through adult.", /* 7 -- ball */
                "-", /* 8 */
                "-", /* 9 */
                "i", /* 10 -- book */
                "Do not use in enclosed spaces.", /* 11 -- boomerang */
                "BOTTLE: GET from water source to fill.  PUT at target to pour.", /*
                 * 12
                 * --
                 * bottle
                 */
                "i", /* 13 -- box */
                "-", /* 14 */
                "-", /* 15 */
                "User assumes all responsibility for consequences of use.", /*
                 * 16
                 * --
                 * club
                 */
                "COMPASS: Arrow points towards West Pole.", /* 17 -- compass */
                "Acme Countertop Co.", /* 18 -- countertop */
                "-", /* 19 */
                "Fragile, do not drop.", /* 20 -- crystal ball */
                "DIE: DO rolls the die", /* 21 -- die */
                "Acme Display Case Co., Fnelia", /* 22 -- display case */
                "i", /* 23 -- door */
                "Don't ever antagonize the horn.", /* 24 -- dropbox */
                "Take only as directed.  Select DO to consume.", /*
                 * 25 -- drugs
                 */
                "Select DO to activate.", /* 26 -- escape device */
                "Use with care.", /* 27 -- fake gun */
                "i", /* 28 -- elevator */
                "\"Soldier ask not, now or ever, where to war your banners go...\"", /*
                 * 29
                 * --
                 * flag
                 */
                "i", /* 30 -- flashlight */
                "Do not use near powerlines.", /* 31 -- frisbee */
                "GARBAGE CAN: DO flushes contents.", /* 32 -- garbage can */
                "i", /* 33 -- gemstone */
                "-", /* 34 */
                "i", /* 35 -- grenade */
                "s", /* 36 -- ground */
                "Use with care.", /* 37 -- gun */
                "How dare you!", /* 38 -- hand of god */
                "i", /* 39 -- hat */
                "Add water to activate.", /* 40 -- instant object pill */
                "i", /* 41 -- jacket */
                "KEY: Hold while opening or closing door or container, if key matches lock, it will lock or unlock it.", /*
                 * 42
                 * --
                 * key
                 */
                "i", /* 43 -- knick knack */
                "Point sharp end towards victim.", /* 44 -- knife */
                "MAGIC LAMP: DO rubs lamp and calls Genie.  TALK to Genie to make wish.  Phrase your wish *carefully*!", /*
                 * 45
                 * --
                 * magic
                 * lamp
                 */
                "i", /* 46 -- magic staff */
                "i", /* 47 -- magic wand */
                "We Await Silent Tristero's Empire", /* 48 -- mailbox */
                "You too can be a highly paid universe designer.  Contact... (the rest is illegible, I'm afraid)", /*
                 * 49
                 * --
                 * matchbook
                 */
                "-", /* 50 */
                "-", /* 51 */
                "Select DO to turn on or off.", /* 52 -- movie camera */
                "-", /* 53 */
                "PAPER: Select DO to read from or write on paper.", /*
                 * 54 --
                 * paper
                 */
                "i", /* 55 */
                "What's the matter?  Can't you read?", /* 56 -- short sign */
                "What's the matter?  Can't you read?", /* 57 -- sign */
                "Acme Landscaping Company", /* 58 -- plant */
                "-", /* 59 */
                "i", /* 60 -- ring */
                "Acme Quarries, Ltd.", /* 61 -- rock */
                "-", /* 62 */
                "Select DO to turn on or off.", /* 63 -- security device */
                "i", /* 64 -- sensor */
                "-", /* 65 */
                "-", /* 66 */
                "-", /* 67 */
                "-", /* 68 */
                "s", /* 69 -- sky */
                "u", /* 70 -- stereo */
                "u", /* 71 -- tape */
                "-", /* 72 */
                "-", /* 73 */
                "i", /* 74 -- teleport booth */
                "i", /* 75 -- ticket */
                "TOKENS: DO displays denomination.", /* 76 -- tokens */
                "-", /* 77 */
                "-", /* 78 */
                "-", /* 79 */
                "s", /* 80 -- wall */
                "-", /* 81 */
                "Select DO to wind.", /* 82 -- windup toy */
                "-", /* 83 */
                "CHANGE-O-MATIC: Point at wall or furniture, then select DO.  Works only in your Turf.", /*
                 * 84
                 * --
                 * changomatic
                 */
                "VENDO: DO displays next selection.  PUT tokens here to purchase item on display.", /*
                 * 85
                 * --
                 * vendo
                 * front
                 */
                "i", /* 86 -- vendo inside */
                "s", /* 87 -- trapezoid */
                "s", /* 88 -- hole */
                "SHOVEL: Point at ground and select DO to dig.", /*
                 * 89 -- shovel
                 */
                "CHANGE MACHINE: Select DO for change.", /* 90 -- sex changer */
                "STUN GUN: do not overuse.", /* 91 -- stun gun */
                "s", /* 92 -- super trapezoid */
                "s", /* 93 -- flat */
                "This is a test.  Had this been an actual object this message would have meaningful content.", /*
                 * 94
                 * --
                 * test
                 */
                "BODY SPRAYER: Point at desired limb, then select DO to color that limb.", /*
                 * 95
                 * --
                 * spray
                 * can
                 */
                "PAWN MACHINE: PUT item inside, then DO to receive tokens in exchange for item", /*
                 * 96
                 * --
                 * pawn
                 * machine
                 */
                "i", /* 97 -- switch / immobile magic */
                "s", /* 98 -- "glue" */
                "-", /* 99 */
                "-", /* 100 */
                "-", /* 101 */
                "-", /* 102 */
                "-", /* 103 */
                "-", /* 104 */
                "-", /* 105 */
                "-", /* 106 */
                "-", /* 107 */
                "-", /* 108 */
                "-", /* 109 */
                "-", /* 110 */
                "-", /* 111 */
                "-", /* 112 */
                "-", /* 113 */
                "-", /* 114 */
                "-", /* 115 */
                "-", /* 116 */
                "-", /* 117 */
                "-", /* 118 */
                "-", /* 119 */
                "-", /* 120 */
                "-", /* 121 */
                "-", /* 122 */
                "-", /* 123 */
                "-", /* 124 */
                "-", /* 125 */
                "-", /* 126 */
                "i", /* 127 -- head */
                "-", /* 128 */
                "Glub, glub.  Two fish in a tub.", /* 129 -- aquarium */
                "BED: If standing by bed, point at it and select GO to sit.  If sitting, point at bed and GO to stand again.", /*
                 * 130
                 * --
                 * bed
                 */
                "\"Beware of troll\"", /* 131 -- bridge */
                "\"An Avatar's Turf is his castle.\"", /* 132 -- building */
                "Acme Landscaping Co.", /* 133 -- bush */
                "CHAIR: If standing by chair, point at it and select GO to sit.  If sitting, point at chair and GO to stand again.", /*
                 * 134
                 * --
                 * chair
                 */
                "i", /* 135 -- chest */
                "\"Have A Choke!\"  Insert coin.", /* 136 -- coke machine */
                "COUCH: If standing by couch, point at it and select GO to sit.  If sitting, point at couch and GO to stand again.", /*
                 * 137
                 * --
                 * couch
                 */
                "Acme Fence Co.", /* 138 -- fence */
                "i", /* 139 -- floor lamp */
                "PUT tokens for significant message.", /*
                 * 140 -- fortune machine
                 */
                "FOUNTAIN: TALK sends message to the Oracle.  Phrase your question or request *carefully*!", /*
                 * 141
                 * --
                 * fountain
                 */
                "-", /* 142 */
                "\"Meow!\"", /* 143 -- house cat */
                "Acme Hot Tub Co., Marin, California", /* 144 -- hot tub */
                "u", /* 145 -- jukebox */
                "-", /* 146 */
                "s", /* 147 -- pond */
                "s", /* 148 -- river */
                "i", /* 149 -- roof */
                "i", /* 150 -- safe */
                "-", /* 151 */
                "What's the matter?  You blind?  It's a picture.", /*
                 * 152 --
                 * picture
                 */
                "s", /* 153 -- street */
                "Acme Streetlamp Co.", /* 154 -- streetlamp */
                "Acme Table Co.", /* 155 -- table */
                "Acme Landscaping Co.", /* 156 -- tree */
                "Acme Window Co.", /* 157 -- window */
        "BUREAUCRAT: TALK sends your request to the bureaucracy.  Please be sure this is the right bureaucrat." };

        String the_message = help_messages[HabitatClass()];
        if (the_message == "-") { /* non-existent objects */
            the_message = "This object does not exist.";
        } else if (the_message == "s") { /* background scenic objects */
            the_message = "For HELP, point at an object and press the F7 key. Press F8 for general tips.";
        } else if (the_message == "u") { /* unimplemented help features */
            the_message = "Sorry, no help here yet.";
        } else if (the_message == "i") { /* impossible to get messages */
            trace_msg("Impossible help request, class " + this.getClass().getSimpleName()
                    + ". Missing HELP implementation?");
            the_message = "How did you do that?";
        }
        send_reply_msg(from, the_message);
    }

    /**
     * @param packedBits
     *            The bits unpacked into a boolean array. NOTE: PL1 arrays uses
     *            1-based arrays, so historically all the bit offset constants
     *            are as well. We lose the high bit, but we never use it.
     *
     * @return boolean array of unpacked bits
     */
    public boolean[] unpackBits(int packedBits) {
        boolean bits[] = new boolean[32];
        for (int i = 0; i < 31; i++) {
            bits[i + 1] = ((packedBits & (1 << i)) != 0);
        }
        return bits;
    }

    /**
     * NOTE: PL1 arrays uses 1-based arrays, so historically all the bit offset
     * constants are as well. We lose the high bit, but we never use it.
     * 
     * @param bits
     *            The boolean array to pack into an int.
     * @return an int made of the the bits
     */
    public int packBits(boolean[] bits) {
        int result = 0;
        for (int i = 0; i < 31; ++i) {
            if (bits[i + 1]) {
                result = result | (1 << i);
            }
        }
        return result;
    }

    /**
     * This is a special-case visibility check. You see, the avatar is both
     * transparent AND opaque.
     * 
     * @param cont
     *            The container being tested
     * @param pos
     *            The position being tested
     * @return Is this slot, of this container, visible to the region?
     */
    public boolean container_is_opaque(HabitatMod cont, int pos) {
        if (cont.HabitatClass() == CLASS_AVATAR)
            if (pos == HANDS || pos == HEAD)
                return (false);
            else
                return (true);
        return cont.opaque_container();
    }

    /**
     * empty_handed -- Return true iff 'who' is not holding anything.
     * 
     * @param who
     *            The avatar being tested.
     * @return Is the avatar empty handed?
     */
    public boolean empty_handed(Avatar who) {
        return (who.contents(HANDS) == null);
    }

    /**
     * holding -- Return true iff the avatar is holding a given object.
     * 
     * @param avatar
     *            The avatar being tested.
     * @param object
     *            The object being tested.
     * @return Is the object in the avatar's hands?
     */

    public boolean holding(Avatar avatar, HabitatMod object) {
        HabitatMod inHands = avatar.contents(HANDS);
        return (null != inHands && inHands.noid == object.noid);
    }


    /**
     * Returns the HabitatMod of the item held in the avatar's hand.
     * 
     * @param avatar
     * @return
     */
    public HabitatMod heldObject(Avatar avatar) {
        return avatar.contents(HANDS);
    }


    /**
     * Returns the HabitatMod of the item held in the user's avatar's hand.
     * 
     * @param from
     * @return
     */
    public HabitatMod heldObject(User from) {
        return heldObject(avatar(from));
    }

    /**
     * Returns the HabitatMod of the item held in *this* avatar object.
     * 
     * @return
     */
    public HabitatMod heldObject() {
        return heldObject((Avatar) this);
    }

    /**
     * wearing -- Return true iff the avatar is wearing (head slot) a given
     * object.
     * 
     * @param avatar
     *            The avatar being tested.
     * @param object
     *            The object being tested.
     * @return Is the object being worn by the avatar?
     */
    public boolean wearing(Avatar avatar, HabitatMod object) {
        HabitatMod onShoulders = avatar.contents(HEAD);
        return (null != onShoulders && onShoulders.noid == object.noid);
    }

    /**
     * getable -- Return true iff a given object can be picked up by an avatar.
     *
     * @param object
     *            The object being tested.
     * @return Is the object portable?
     */
    public boolean getable(HabitatMod object) {
        if (object.HabitatClass() == CLASS_ROCK 
                || object.HabitatClass() == CLASS_FLAG 
                || object.HabitatClass() == CLASS_PLANT) {
            return (((Massive) object).mass == 0);
        }
        return true;
    }

    public boolean immobile(HabitatMod object) {
        switch (object.HabitatClass()) {
            case CLASS_ATM: 
            case CLASS_BED:
            case CLASS_BUILDING:
            case CLASS_BUSH:
            case CLASS_CHAIR:
            case CLASS_CHANGOMATIC:
            case CLASS_CHEST:
            case CLASS_COKE_MACHINE:
            case CLASS_COUCH:
            case CLASS_COUNTERTOP:
            case CLASS_DISPLAY_CASE:
            case CLASS_DOOR:
            case CLASS_DROPBOX:
            case CLASS_ELEVATOR:
            case CLASS_FENCE:
            case CLASS_FLAT:
            case CLASS_FLOOR_LAMP:
            case CLASS_FORTUNE_MACHINE:
            case CLASS_FOUNTAIN:
            case CLASS_GARBAGE_CAN:
            case CLASS_GHOST:
            case CLASS_GLUE:
            case CLASS_GROUND:
            case CLASS_HOLE:
            case CLASS_HOT_TUB:
            case CLASS_HOUSE_CAT:
            case CLASS_MAGIC_IMMOBILE:
            case CLASS_MAILBOX:
            case CLASS_PAWN_MACHINE:
            case CLASS_PICTURE:
            case CLASS_PLAQUE:
            case CLASS_POND:
            case CLASS_ROOF:
            case CLASS_SAFE:
            case CLASS_SEX_CHANGER:
            case CLASS_SHORT_SIGN:
            case CLASS_SIGN:
            case CLASS_SKY:
            case CLASS_STREET:
            case CLASS_STREETLAMP:
            case CLASS_SUPER_TRAPEZOID:
            case CLASS_TABLE:
            case CLASS_TELEPORT:
            case CLASS_TRAPEZOID:
            case CLASS_TREE:
            case CLASS_VENDO_FRONT:
            case CLASS_VENDO_INSIDE:
            case CLASS_WALL:
            case CLASS_WINDOW:
                return true;            
            default:
                break;
        }
        return !getable(object);
    }
    

    /**
     * grabable -- Return true iff a given object can be grabbed from an
     * avatar's hand.
     * 
     * NOTE: Tests the region to see if it is STEAL_FREE.
     *
     * @param object
     *            The object being tested.
     * @return Is the object grabable?
     */
    public boolean grabable(HabitatMod object) {

        if (current_region().nitty_bits[STEAL_FREE] || object.HabitatClass() == CLASS_PAPER
                || object.HabitatClass() == CLASS_BOOK || object.HabitatClass() == CLASS_TOKENS
                || (object.HabitatClass() == CLASS_MAGIC_LAMP && object.gr_state == MAGIC_LAMP_GENIE)) {
            return false;
        }
        return true;
    }

    /**
     * Is the specified position available to be filled?
     * 
     * @param container
     *            The target container;
     * @param x
     *            The horizontal position (not considered. Should be deprecated)
     * @param y
     *            The slot/vertical position
     * @return Is it OK to put the item at this position?
     */
    public boolean available(HabitatMod container, int x, int y) {
        if (container == null)
            return false;
        if (container.noid == THE_REGION)
            return true;
        if (!(container instanceof Container))
            return false;
        return ((Container) container).contents(y) == null;
    }

    /**
     * accessable -- Return true iff a given object can be reached by the
     * avatar.
     * 
     * @param object
     *            The object being tested.
     * @return Can we access this object given container nesting??
     */
    public boolean accessable(HabitatMod object, User from) {
        if (container(object).noid == THE_REGION)
            return (adjacent(object, from));
        else
            /*
             * return(accessable(ObjList(object.container))); TODO Placeholder
             * Recursive container walk
             */
            return true;
    }

    /**
     * elsewhere -- Return true iff the object is not near the Avatar (i.e., not
     * adjacent and not in hand).
     * 
     * @param object
     *            The object being tested.
     * @param user
     *            The User-avatar
     * @return Is our avatar standing in the right place to manipulate this
     *         object?
     */
    public boolean elsewhere(HabitatMod object, User user) {
        return (container(object) != (Container) avatar(user) && !adjacent(object, user));
    }

    /**
     * here -- Return true iff the given object is exactly where the Avatar is.
     * 
     * @param object
     *            The object being tested.
     * @param user
     *            The User-avatar
     * @return Is our avatar standing exactly on the object?
     */
    public boolean here(HabitatMod object, User user) {
        return (container(object).noid == THE_REGION && object.x == avatar(user).x && object.y == avatar(user).y);
    }

    
    /**
     * goto_new_region -- Transfer the avatar to someplace else.
     * 
     * Provided as syntactic sugar for easy class migration.
     * 
     * @param avatar The avatar mod of the user that is being sent someplace else
     * @param contextRef The elko ref for the new region
     * @param direction WEST, EAST, NORTH, SOUTH, AUTO_TELEPORT_DIR
     * @param transition_type WALK_ENTRY, TELEPORT_ENTRY, DEATH_ENTRY
     * @param x Position on arrival (0 means the avatar should choose)
     * @param y Position on arrival (0 means the avatar should choose)
     */

    public void goto_new_region(Avatar avatar, String contextRef, int direction, int transition_type, int x, int y) {
        // This used to be more complicated - Elko's ever-context-change-is-a-new-connection makes this easy.
        // We just ask the bridge to call us back for a new region.
        avatar.change_regions(contextRef, direction, transition_type, x, y);
    }

    
    /**
     * goto_new_region -- Transfer the avatar to someplace else.
     * 
     * Provided as syntactic sugar for easy class migration.
     * 
     * @param avatar The avatar mod of the user that is being sent someplace else
     * @param contextRef The elko ref for the new region
     * @param direction WEST, EAST, NORTH, SOUTH, AUTO_TELEPORT_DIR
     * @param transition_type WALK_ENTRY, TELEPORT_ENTRY, DEATH_ENTRY
     */

    public void goto_new_region(Avatar avatar, String contextRef, int direction, int transition_type) {
        // This used to be more complicated - Elko's ever-context-change-is-a-new-connection makes this easy.
        // We just ask the bridge to call us back for a new region.
        goto_new_region(avatar, contextRef, direction, transition_type, 0, 0);
    }

    /**
    * Are we standing next to the object so we can manipulate it properly?
    * 
    * @param object
    *            The object being tested.
    * @return Is our avatar standing next to the object?
    */
    public boolean adjacent(HabitatMod object, User from) {
        /*
        Avatar curAvatar = avatar(from);
        int obase = image_base[object.HabitatClass()] + object.style;
        int x_right, x_left, y;
        int X_MAX = 156;
        if(object.noid == curAvatar.noid) {
            return true;
        }
    
        //JSN: Is this PL/1 hack really necessary?
        if(object.HabitatClass() == CLASS_HEAD) {
            return true;
        }
    
        if(test_bit(object.orientation, ORIENTATION_BIT)){
            x_right = object.x - image_x_right[obase] - image_celWidth[obase];
            x_left = object.x - image_x_left[obase] - image_celWidth[obase];
        }
        else
            x_right = object.x + image_x_right[obase];
            x_left = object.x + image_x_left[obase];
    
        y = object.y;
        y = clear_bit(y, 8);
        y = y + image_y[obase];
    
        if(y < 0) {
            y = 0;
        }
        if(y > current_region().depth) {
            y = current_region().depth;
        }
        y = clear_bit(y, 1);

        if(x_right < 0) {
            x_right = 0;
        }
        else if(x_right > X_MAX){
            x_right = X_MAX;
        }
        x_right = clear_bit(x_right, 1);
        x_right = clear_bit(x_right, 2);
    
        if(x_left < 0) {
            x_left = 0;
        }
        else if(x_left > X_MAX){
            x_left = X_MAX;
        }
        x_left = clear_bit(x_left, 1);
        x_left = clear_bit(x_left, 2);
    
        int av_x = curAvatar.x;
        av_x = clear_bit(av_x, 1);
        av_x = clear_bit(av_x, 2);
    
        int av_y = curAvatar.y;
        av_y = clear_bit(av_y, 1);
        av_y = clear_bit(av_y, 8);
        trace_msg("ax_x=" + av_x + " x_left=" + x_left + " x_right=" + x_right);
        trace_msg("av_y=" + av_y + " y="+ y );
        return (av_x == x_left || av_x == x_right) && av_y == y;
    */
        return true; //JSN: Uncomment when adjacency is fixed
    }


    /**
     * change_containers -- Move an object from one container to another.
     * 
     * @param obj
     *            The object being moved
     * @param new_container
     *            The target container
     * @param new_position
     *            The new position (slot)
     * @param cp
     *            Checkpoint flag
     * @return success
     */

    public boolean change_containers(HabitatMod obj, Container new_container, int new_position, boolean cp) {

        if (obj.noid == THE_REGION) {
            trace_msg("*ERR* Attempt to contain region: " + obj.object().ref());
            return false;
        }

        fits = heap_space_available(obj, new_container, new_position);
        if (!fits)
            return false;

        /*
         * Every check has passed. Proceed with container change (which should
         * not fail.)
         */

        ((Item) obj.object()).setContainer(new_container.object());

        obj.y = new_position;
        obj.gen_flags[MODIFIED] = true;
        if (cp) {
            obj.checkpoint_object(obj);
        }

        return true;

    }
    
    /**
     * A full object (all resources) is about to be loaded in every C64
     * client that can see this region. Account for the memory use of
     * every resource (class def, images, head hack, actions, sounds)
     * 
     * @param obj
     */
    public void note_object_creation(HabitatMod obj) {
        int class_number= obj.HabitatClass();
        int style       = obj.style;

        if (class_number < 0  || class_number > MAX_CLASS_NUMBER) {
            trace_msg("*ERR* call to CAPMON create with class = " + obj.HabitatModName());
            return;
        }

        note_instance_creation_internal(obj);
        note_resource_creation_internal(obj, style);

    }
    
    /**
     * Account for ONLY the instance data and class overhead loaded for an object
     * to the C64 memory heap. Do not yet account for the image, sound, of behavior
     * resources. (This is what we mean by an 'opaque' container.)
     * 
     * @param obj
     */
    
    public void note_instance_creation(HabitatMod obj) {
        int class_number= obj.HabitatClass();

        if (class_number < 0  || class_number > MAX_CLASS_NUMBER) {
            trace_msg("*ERR* call to CAPMON create with class = " + obj.HabitatModName());
            return;
        }
        note_instance_creation_internal(obj);
    }
    
    private void note_instance_creation_internal(HabitatMod obj) {
        int class_number= obj.HabitatClass();
        Region region   = current_region();

        region.space_usage += obj.instance_size();
        region.class_ref_count[class_number]++;
        if (region.class_ref_count[class_number] == 1)
            region.space_usage += obj.class_size();
    }

        private void note_resource_creation_internal(HabitatMod obj, int style) {
        int     class_number= obj.HabitatClass();

        int type = NeoHabitat.RESOURCE_IMAGE;
        if (class_number == CLASS_HEAD)
            note_resource_usage(NeoHabitat.RESOURCE_HEAD, style);
        else if (NeoHabitat.ClassResources[class_number][type].length > 0)
            note_resource_usage(type, style);

        type++;     
        while (type <= NeoHabitat.RESOURCE_SOUND) {
            for (int i = 0; i < NeoHabitat.ClassResources[class_number][type].length; i++) {
                note_resource_usage(type, NeoHabitat.ClassResources[class_number][type][i]);
            }
            type++;
        }
    }

    private void note_resource_usage(int type, int resource) {
        Region region = current_region();
        
        region.resource_ref_count[type][resource] += 1;
         if (region.resource_ref_count[type][resource] == 1)
              region.space_usage += NeoHabitat.ResourceSizes[type][resource];       
    }
    
    /**
     * Recover and object and it's class resources from the C64 heap.
     * 
     * @param obj
     */

    public void note_object_deletion(HabitatMod obj) {
        int class_number= obj.HabitatClass();
        int style       = obj.style;

        if (class_number < 0  || class_number > MAX_CLASS_NUMBER) {
            trace_msg("*ERR* call to CAPMON delete with class = " + obj.HabitatModName());
            return;
        }

        note_instance_deletion_internal(obj);
        note_resource_deletion_internal(obj, style);
    }

    /**
     * Recover only the instance space from the C64 heap model for this region.
     * 
     * @param obj
     */
    public void note_instance_deletion(HabitatMod obj) {
        int class_number= obj.HabitatClass();
        int style       = obj.style;

        if (class_number < 0  || class_number > MAX_CLASS_NUMBER) {
            trace_msg("*ERR* call to CAPMON delete with class = " + obj.HabitatModName());
            return;
        }

        note_instance_deletion_internal(obj);
    }
    
    private void note_instance_deletion_internal(HabitatMod obj) {
        int class_number= obj.HabitatClass();
        Region region   = current_region();

        region.space_usage -= obj.instance_size();
        region.class_ref_count[class_number]--;
        if (region.class_ref_count[class_number] == 0)
            region.space_usage -= obj.class_size();
    }

    private void note_resource_deletion_internal(HabitatMod obj, int style) {
        int     class_number= obj.HabitatClass();

        int type = NeoHabitat.RESOURCE_IMAGE;
        if (class_number == CLASS_HEAD)
            note_resource_removal(NeoHabitat.RESOURCE_HEAD, style);
        else if (NeoHabitat.ClassResources[class_number][type].length > 0) 
            note_resource_removal(type, style);

        type++;     
        while (type <= NeoHabitat.RESOURCE_SOUND) {
            for (int i = 0; i < NeoHabitat.ClassResources[class_number][type].length; i++) {
                note_resource_removal(type, NeoHabitat.ClassResources[class_number][type][i]);
            }
            type++;
        }
    }

    private void note_resource_removal(int type, int resource) {
        Region region = current_region();
        
        region.resource_ref_count[type][resource] -= 1;
         if (region.resource_ref_count[type][resource] == 0)
              region.space_usage -= NeoHabitat.ResourceSizes[type][resource];       
    }
    
    
    /**
     * Check to make sure various limits related to the C64 heap manager
     * are still within limits.
     * 
     * The "head hack" limits 32 heads in a region (mapping 255 head types to 32 fixed image slots)
     * Apparently the client can only allow 64 instances of any class in a region (bit packing, I'm sure.)
     * Of course, total memory usage is limited as well.
     * 
     * NOTE! C64_HEAP_SIZE changes with client builds, so the server needs to know the SMALLEST of these 
     * if there are ever multiple clients.
     * 
     * @param obj
     * @return
     */
    public boolean mem_checks_ok(HabitatMod obj) {
        
        Region region = current_region();
        if (region.space_usage > C64_HEAP_SIZE)
            return false;        

        int the_class = obj.HabitatClass();
        if (the_class == CLASS_HEAD)
            if (region.class_ref_count[the_class] > 31)
                return false;
        
        if (region.class_ref_count[the_class] > 63)
            return false;

        return true;
    }

    public boolean mem_check_container(Container cont) {
        for (int i = 0; i < cont.capacity(); i++) {
            HabitatMod obj = cont.contents(i);
            if (obj != null)
                if (false == mem_checks_ok(obj))
                    return false;
        }
        return true;
    }
    
    public int class_size() {
        return NeoHabitat.ClassSizes[HabitatClass()];
    }
    
    public int instance_size() {
        return 6 + this.capacity() + pc_state_bytes();
    }
    
    
    // END HACK STUBS

    /**
     * Test to see if the client will have room for the resources changed by an
     * upcoming container change. This can be a problem when an object comes out
     * of an opaque container.
     * 
     * @param obj
     *            Object being moved
     * @param new_container
     *            The new container.
     * @return Will the container change work on the client, considering limited
     *         memory?
     */

    public boolean heap_space_available(HabitatMod obj, Container new_container, int new_position) {

        HabitatMod old_container = obj.container();
        
        if (container_is_opaque(old_container, obj.y)) {
            note_instance_deletion(obj);
        } else {
            note_object_deletion(obj);
        }
        if (container_is_opaque(new_container, new_position)) {
            note_instance_creation(obj);
        } else {
            note_object_creation(obj);
        }
        
        if (mem_checks_ok(obj)) {
            return true;
        }

        if (container_is_opaque(new_container, new_position)) {
            note_instance_deletion(obj);
        } else {
            note_object_deletion(obj);
        }
        if (container_is_opaque(old_container, obj.y)) {
            note_instance_creation(obj);
        } else {
            note_object_creation(obj);
        }
        
        return false;
    }

    /**
     * Dump a string to the debugging server log.
     * 
     * @param msg
     *            The message to log, with optional java.util.Formatter options (%s, %d, etc.)
     * @param args
     *            Parameters to format into the string
     */
    public void trace_msg(String msg, Object... args) {
        Trace.trace("habitat").warningm(new Formatter().format(msg, args).toString());
    }

    /**
     * Logs a Throwable to the trace log.
     *
     * @param t a Throwable to log the trace from
     */
    public void trace_exception(Throwable t) {
        trace_msg("Caught a Neohabitat exception:\n%s", getTracebackString(t));
    }

    /**
     * Message from a Habitat user that is meant to be logged in a special
     * Oracle/Moderator log file. Often a message about a special (hard coded)
     * event or speaking with the Oracle Fountain.
     * 
     * @param obj
     *            The object that is logging the message (e.g. Oracle Fountain.)
     * @param avatar
     *            The avatar that took the action to trigger the message.
     * @param msg
     *            The message to be logged.
     */
    public void message_to_god(HabitatMod obj, HabitatMod avatar, String msg) {
        trace_msg(msg + " by " + avatar.object().ref() + " via " + obj.object().ref());
        /*
         * TODO Placeholder implementation by FRF - full implementation when
         * CLASS_ORACLE is ported.
         */
    }

    /**
     * An object sends a string message to a specific user
     * 
     * @param to
     *            User the message is going to.
     * @param noid
     *            The object speaking to the user.
     * @param text
     *            What the object wants to say.
     */
    public void object_say(User to, int noid, String text) {
        JSONLiteral msg = new_private_msg(THE_REGION, "OBJECTSPEAK_$");
        msg.addParameter("text", text);
        msg.addParameter("speaker", noid);
        msg.finish();
        to.send(msg);
    }

    /**
     * An object sends a string message to a specific user
     * 
     * @param to
     *            User the message is going to.
     * @param text
     *            What the object wants to say.
     */
    public void object_say(User to, String text) {
        object_say(to, this.noid, text);
    }

    /**
     * An object sends a string message to everyone
     * 
     * @param noid
     *            The object speaking to the region.
     * @param text
     *            What the object wants to say.
     */

    public void object_broadcast(int noid, String text) {
        JSONLiteral msg = new_broadcast_msg(THE_REGION, "OBJECTSPEAK_$");
        msg.addParameter("text", text);
        msg.addParameter("speaker", noid);
        msg.finish();
        context().send(msg);
    }

    /**
     * An object sends a string message to a User's neighbors
     *
     * @param noid
     *            The object speaking to the User's neighbors.
     * @param text
     *            What the object wants to say.
     */
    public void object_say_to_neighbors(User from, int noid, String text) {
        send_neighbor_msg(from, THE_REGION, "OBJECTSPEAK_$",
            "speaker", noid,
            "text", text);
    }

    /**
     * An object sends a string message to everyone
     * 
     * @param text
     *            What the object wants to say.
     */
    public void object_broadcast(String text) {
        object_broadcast(this.noid, text);
    }
    
    /**
     * All fiddle message share the same arguments. This adds those no matter what the message routing type.
     * 
     * @param msg
     * @param target
     * @param offset
     * @param args
     */
    public void compose_fiddle_msg(JSONLiteral msg, int target, int offset, int[] args) {
        msg.addParameter("target", target);
        msg.addParameter("offset", offset);
        msg.addParameter("argCount", args.length);
        if (args.length > 1) {
            msg.addParameter("value", args);
        } else {
            msg.addParameter("value", args[0]);
        }
        msg.finish();
        return;
    }

    /**
     * Send a point to point (aka private) fiddle message.
     * 
     * @param from
     * @param to
     * @param noid
     * @param target
     * @param offset
     * @param args
     */
    public void send_private_fiddle_msg(User from, User to, int noid, int target, int offset, int[] args) {
        JSONLiteral msg = new_private_msg(noid, "FIDDLE_$");
        compose_fiddle_msg(msg, target, offset, args);
        to.send(msg);
    }

    /**
     * Send private fiddle message with only one arg
     * 
     * @param from
     * @param to
     * @param noid
     * @param target
     * @param offset
     * @param arg
     */
    public void send_private_fiddle_msg(User from, User to, int noid, int target, int offset, int arg) {
        send_private_fiddle_msg(from, to, noid, target, offset, new int[]{ arg });
    }

    /**
     * Send neighbors a fiddle message.
     * 
     * @param from
     * @param noid
     * @param target
     * @param offset
     * @param args
     */
    public void send_neighbor_fiddle_msg(User from, int noid, int target, int offset, int[] args) {
        JSONLiteral msg = new_neighbor_msg(noid, "FIDDLE_$");
        compose_fiddle_msg(msg, target, offset, args);
        context().sendToNeighbors(from, msg);
    }       
    
    /**
     * Send the neighbors a fiddle messgae with only one arg.
     * 
     * @param from
     * @param noid
     * @param target
     * @param offset
     * @param arg
     */
    public void send_neighbor_fiddle_msg(User from, int noid, int target, int offset, int arg) {
        send_neighbor_fiddle_msg(from, noid, target, offset, new int[]{ arg });
    }

    /**
     * Send a fiddle message to the entire region. The client does all the work.
     *
     * @param noid
     * @param args
     */
    public void send_fiddle_msg(int noid, int target, int offset, int[] args) {
        JSONLiteral msg = new_broadcast_msg(noid, "FIDDLE_$");
        compose_fiddle_msg(msg, target, offset, args);
        context().send(msg);
    }


    /**
     * Fiddle message with only a single arg...
     * 
     * @param noid
     * @param target
     * @param offset
     * @param arg
     */
    public void send_fiddle_msg(int noid, int target, int offset, int arg) {
        send_fiddle_msg(noid, target, offset, new int[]{ arg });
    }

    /**
     * Tells the region to get rid of an object at the provided noid.
     *
     * @param noid object noid to eliminate
     */
    public void send_goaway_msg(int noid) {
        JSONLiteral msg = new_broadcast_msg(THE_REGION, "GOAWAY_$");
        msg.addParameter("target", noid);
        msg.finish();
        context().send(msg);
    }

    /**
     * Tells the clients of the neighbors of an avatar to get rid of
     * an object.
     *
     * @param noid object noid to eliminate
     */
    public void send_neighbor_goaway_msg(User from, int noid) {
        send_neighbor_msg(from, THE_REGION, "GOAWAY_$",
            "target", noid);
    }

    /**
     * Temporary scaffolding for incremental development of the server. Call
     * this to say "not ready yet!" and reply with an error code. Hopefully the
     * client will accept the result and proceed.
     * 
     * @param from
     *            The connection for this user.
     * @param noid
     *            The noid that sent the request that is being unceremoniously
     *            terminated.
     * @param text
     *            This error message text will be sent to the client of the user
     *            that issued the unsupported command.
     */
    public void unsupported_reply(User from, int noid, String text) {
        object_say(from, text);
        send_reply_error(from, noid); // TODO Remove This last ditch attempt to
        // keep the client running after a
        // unsupported command arrives.
    }

    /**
     * Unexpected client message in an illegal state. Does not attempt to rescue the client.
     * 
     * @param from
     * @param text
     */
    public void illegal_request(User from, String text) {
        object_say(from, text); 
        trace_msg(text);
    }
    
    /**
     * Create a JSONLiteral initialized with the minimum arguments for broadcast
     * from the Habitat/Elko server.
     * 
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request. In the PL/1 source
     *            this was a numeric constant, not a string. i.e PL/1: SPEAK$
     *            Elko: "SPEAK$". The lookup now occurs in the Client/Server
     *            bridge.
     * @return message ready to add more parameters, finish(), and send.
     */
    public JSONLiteral new_broadcast_msg(int noid, String op) {
        JSONLiteral msg = new JSONLiteral("broadcast", EncodeControl.forClient);
        msg.addParameter("noid", noid);
        msg.addParameter("op", op);
        return msg;
    }

    /**
     * Create a JSONLiteral initialized with the minimum arguments needed for
     * the Habitat/Elko server. Assumes this.noid is the object of interest.
     * 
     * @param op
     *            The STRING name of the ASYNCRONOUS request. In the PL/1 source
     *            this was a numeric constant, not a string. i.e PL/1: SPEAK$
     *            Elko: "SPEAK$". The lookup now occurs in the Client/Server
     *            bridge.
     * @return message ready to add more parameters, finish(), and send.
     */
    public JSONLiteral new_broadcast_msg(String op) {
        return new_broadcast_msg(this.noid, op);
    }

    /**
     * Sends a ASYNCHRONOUS broadcast message to all the
     * connections/users/avatars in a region.
     * 
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_broadcast_msg)
     */
    public void send_broadcast_msg(int noid, String op) {
        JSONLiteral msg = new_broadcast_msg(noid, op);
        msg.finish();
        context().send(msg);
    }

    /**
     * Sends a ASYNCHRONOUS broadcast message to all the
     * connections/users/avatars in a region.
     * 
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_broadcast_msg)
     * @param text
     *            A string to send, will have attribute name "text"
     */
    public void send_broadcast_msg(int noid, String op, String text) {
        JSONLiteral msg = new_broadcast_msg(noid, op);
        msg.addParameter("text", text);
        msg.finish();
        context().send(msg);
    }

    /**
     * Sends a ASYNCHRONOUS broadcast message to all the
     * connections/users/avatars in a region.
     * 
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_broadcast_msg)
     * @param attrib
     *            The attribute name to be added to the message
     * @param value
     *            The value of the attribute.
     */
    public void send_broadcast_msg(int noid, String op, String attrib, int value) {
        JSONLiteral msg = new_broadcast_msg(noid, op);
        msg.addParameter(attrib, value);
        msg.finish();
        context().send(msg);
    }

    /**
     * Sends a ASYNCHRONOUS broadcast message to all the
     * connections/users/avatars in a region.
     * 
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_broadcast_msg)
     * @param attrib
     *            The attribute name to be added to the message
     * @param value
     *            The string value of the attribute.
     */
    public void send_broadcast_msg(int noid, String op, String attrib, String value) {
        JSONLiteral msg = new_broadcast_msg(noid, op);
        msg.addParameter(attrib, value);
        msg.finish();
        context().send(msg);
    }

    /**
     * Sends a ASYNCHRONOUS broadcast message to all the
     * connections/users/avatars in a region.
     * 
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_broadcast_msg)
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     */
    public void send_broadcast_msg(int noid, String op, String a1, int v1, String a2, int v2) {
        JSONLiteral msg = new_broadcast_msg(noid, op);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.finish();
        context().send(msg);
    }

    /**
     * Sends a ASYNCHRONOUS broadcast message to all the
     * connections/users/avatars in a region.
     * 
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_broadcast_msg)
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     * @param a3
     *            Third attribute to add
     * @param v3
     *            Third value to add
     */
    public void send_broadcast_msg(int noid, String op, String a1, int v1, String a2, int v2, String a3, int v3) {
        JSONLiteral msg = new_broadcast_msg(noid, op);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.addParameter(a3, v3);
        msg.finish();
        context().send(msg);
    }

    /**
     * Sends a ASYNCHRONOUS broadcast message to all the
     * connections/users/avatars in a region.
     * 
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_broadcast_msg)
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     * @param a3
     *            Third attribute to add
     * @param v3
     *            Third value to add
     * @param a4
     *            Fourth attribute to add
     * @param v4
     *            Fourth value to add
     */
    public void send_broadcast_msg(int noid, String op, String a1, int v1, String a2, int v2, String a3, int v3,
            String a4, int v4) {
        JSONLiteral msg = new_broadcast_msg(noid, op);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.addParameter(a3, v3);
        msg.addParameter(a4, v4);
        msg.finish();
        context().send(msg);
    }

    /**
     * Creates a SYNCHRONOUS (client is waiting) reply message using the minimum
     * arguments.
     * 
     * @param noid
     *            The object waiting for this reply.
     * @return message ready to add more parameters, finish(), and send.
     */
    public JSONLiteral new_reply_msg(int noid) {
        JSONLiteral msg = new JSONLiteral("reply", EncodeControl.forClient);
        msg.addParameter("noid", noid);
        msg.addParameter("filler", 0); // TODO BAD! WHAT IS THIS??
        return msg;
    }

    /**
     * Generates a reply message assuming that the noid is inferred by this
     * object.
     * 
     * @return message ready to add more parameters, finish(), and send.
     */
    public JSONLiteral new_reply_msg() {
        return new_reply_msg(this.noid);
    }

    /**
     * Sends a SYNCHRONOUS (client is waiting) reply message using the minimum
     * arguments.
     * 
     * @param from
     *            The User/connection that gets the reply.
     * @param noid
     *            The object waiting for this reply.
     */
    public void send_reply_msg(User from, int noid) {
        JSONLiteral msg = new_reply_msg(noid);
        msg.finish();
        from.send(msg);
    }

    /**
     * Sends a SYNCHRONOUS (client is waiting) string-only reply message
     * inferring this.noid.
     * 
     * @param from
     *            The User/connection that gets the reply.
     * @param text
     *            The string to send, added with the attribute name "text"
     */
    public void send_reply_msg(User from, String text) {
        JSONLiteral msg = new_reply_msg();
        msg.addParameter("text", text);
        msg.finish();
        from.send(msg);
    }

    /**
     * Sends a SYNCHRONOUS (client is waiting) reply message, with addition
     * attributes/values.
     * 
     * @param from
     *            The User/connection that gets the reply.
     * @param noid
     *            The object waiting for this reply.
     * @param attrib
     *            The attribute name to be added to the message
     * @param value
     *            The value of the attribute.
     */
    public void send_reply_msg(User from, int noid, String attrib, int value) {
        JSONLiteral msg = new_reply_msg(noid);
        msg.addParameter(attrib, value);
        msg.finish();
        from.send(msg);
    }

    /**
     * Sends a SYNCHRONOUS (client is waiting) reply message, with addition
     * attributes/values.
     * 
     * @param from
     *            The User/connection that gets the reply.
     * @param noid
     *            The object waiting for this reply.
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     */
    public void send_reply_msg(User from, int noid, String a1, int v1, String a2, int v2) {
        JSONLiteral msg = new_reply_msg(noid);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.finish();
        from.send(msg);
    }

    /**
     * Sends a SYNCHRONOUS (client is waiting) reply message, with addition
     * attributes/values.
     * 
     * @param from
     *            The User/connection that gets the reply.
     * @param noid
     *            The object waiting for this reply.
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     * @param a3
     *            Third attribute to add
     * @param v3
     *            Third value to add
     */
    public void send_reply_msg(User from, int noid, String a1, int v1, String a2, int v2, String a3, int v3) {
        JSONLiteral msg = new_reply_msg(noid);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.addParameter(a3, v3);
        msg.finish();
        from.send(msg);
    }

    /**
     * Sends a SYNCHRONOUS (client is waiting) reply message, with addition
     * attributes/values.
     * 
     * @param from
     *            The User/connection that gets the reply.
     * @param noid
     *            The object waiting for this reply.
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     * @param a3
     *            Third attribute to add
     * @param v3
     *            Third value to add
     * @param a4
     *            Fourth attribute to add
     * @param v4
     *            Fourth value to add
     *
     **/
    public void send_reply_msg(User from, int noid, String a1, int v1, String a2, int v2, String a3, int v3, String a4,
            int v4) {
        JSONLiteral msg = new_reply_msg(noid);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.addParameter(a3, v3);
        msg.addParameter(a4, v4);
        msg.finish();
        from.send(msg);
    }

    /**
     * Sends a SYNCHRONOUS (client is waiting) reply message, with addition
     * attributes/values.
     *
     * @param from
     *            The User/connection that gets the reply.
     * @param noid
     *            The object waiting for this reply.
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     * @param a3
     *            Third attribute to add
     * @param v3
     *            Third value to add
     * @param a4
     *            Fourth attribute to add
     * @param v4
     *            Fourth value to add
     *
     **/
    public void send_reply_msg(User from, int noid, String a1, int v1, String a2, int v2, String a3, int v3, String a4,
                               String v4) {
        JSONLiteral msg = new_reply_msg(noid);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.addParameter(a3, v3);
        msg.addParameter(a4, v4);
        msg.finish();
        from.send(msg);
    }

    /**
     * Sends a SYNCHRONOUS (client is waiting) reply message, with additional
     * String value.
     * 
     * @param from
     *            The User/connection that gets the reply.
     * @param noid
     *            The object waiting for this reply.
     * @param attrib
     *            The attribute name to be added to the message
     * @param value
     *            The STRING value of the attribute.
     */
    public void send_reply_msg(User from, int noid, String attrib, String value) {
        JSONLiteral msg = new_reply_msg(noid);
        msg.addParameter(attrib, value);
        msg.finish();
        from.send(msg);
    }

    /**
     * Send simple SYNCHRONOUS reply indicating success or failure.
     * 
     * @param from
     *            The User/connection that gets the reply.
     * @param noid
     *            The object waiting for this reply.
     * @param err
     *            The error state byte (NOT boolean), added to the msg as
     *            attribute "err"
     */
    public void send_reply_err(User from, int noid, int err) {
        JSONLiteral msg = new JSONLiteral("reply", EncodeControl.forClient);
        msg.addParameter("noid", noid);
        msg.addParameter("filler", err); // TODO BAD! WHAT IS THIS??
        msg.addParameter("err", err);
        msg.finish();
        from.send(msg);
    }

    /**
     * Send simple SYNCHRONOUS reply indicating failure.
     * 
     * @param from
     *            The User/connection that gets the reply.
     * @param noid
     *            The object waiting for this reply.
     */
    public void send_reply_error(User from, int noid) {
        send_reply_err(from, noid, FALSE);
    }

    /**
     * Send simple SYNCHRONOUS reply indicating failure. Uses this.noid.
     * 
     * @param from
     *            The User/connection that gets the reply.
     */
    public void send_reply_error(User from) {
        send_reply_error(from, this.noid);
    }

    /**
     * Send simple SYNCHRONOUS reply indicating success.
     * 
     * @param from
     *            The User/connection that gets the reply.
     * @param noid
     *            The object waiting for this reply.
     */
    public void send_reply_success(User from, int noid) {
        send_reply_err(from, noid, TRUE);
    }

    /**
     * Send simple SYNCHRONOUS reply indicating success. Uses this.noid.
     * 
     * @param from
     *            The User/connection that gets the reply.
     */
    public void send_reply_success(User from) {
        send_reply_success(from, this.noid);
    }

    /**
     * Create a JSONLiteral initialized with the minimum arguments to send to a
     * user/connections "neighbors/other users" via the Habitat/Elko server.
     * 
     * @param noid
     *            The object that is acting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request. In the PL/1 source
     *            this was a numeric constant, not a string. i.e PL/1: SPEAK$
     *            Elko: "SPEAK$". The lookup now occurs in the Client/Server
     *            bridge.
     * @return message ready to add more parameters, finish(), and send.
     */
    public JSONLiteral new_neighbor_msg(int noid, String op) {
        JSONLiteral msg = new JSONLiteral("neighbor", EncodeControl.forClient);
        msg.addParameter("noid", noid);
        msg.addParameter("op", op);
        return msg;
    }

    /**
     * Create a JSONLiteral initialized with the minimum arguments to send to a
     * user/connections "neighbors/other users" via the Habitat/Elko server.
     * this.noid is used for the acting object.
     * 
     * @param op
     *            The STRING name of the ASYNCRONOUS request. In the PL/1 source
     *            this was a numeric constant, not a string. i.e PL/1: SPEAK$
     *            Elko: "SPEAK$". The lookup now occurs in the Client/Server
     *            bridge.
     * @return message ready to add more parameters, finish(), and send.
     */
    public JSONLiteral new_neighbor_msg(String op) {
        return new_neighbor_msg(this.noid, op);
    }

    /**
     * Sends a ASYNCHRONOUS message to all the neighbors (other
     * user/connections) in a region.
     * 
     * @param from
     *            The user/connection that is acting, and the only one that will
     *            NOT get the message.
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_neighbor_msg)
     */
    public void send_neighbor_msg(User from, int noid, String op) {
        JSONLiteral msg = new_neighbor_msg(noid, op);
        msg.finish();
        context().sendToNeighbors(from, msg);
    }

    /**
     * Sends a ASYNCHRONOUS message to all the neighbors (other
     * user/connections) in a region. this.noid is the acting object.
     * 
     * @param from
     *            The user/connection that is acting, and the only one that will
     *            NOT get the message.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_neighbor_msg)
     */
    public void send_neighbor_msg(User from, String op) {
        JSONLiteral msg = new_neighbor_msg(this.noid, op);
        msg.finish();
        context().sendToNeighbors(from, msg);
    }

    /**
     * Sends a ASYNCHRONOUS message to all the neighbors (other
     * user/connections) in a region with an additional parameters.
     * 
     * @param from
     *            The user/connection that is acting, and the only one that will
     *            NOT get the message.
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_neighbor_msg)
     * @param attrib
     *            First attribute to add
     * @param value
     *            First value to add
     */
    public void send_neighbor_msg(User from, int noid, String op, String attrib, int value) {
        JSONLiteral msg = new_neighbor_msg(noid, op);
        msg.addParameter(attrib, value);
        msg.finish();
        context().sendToNeighbors(from, msg);
    }

    /**
     * Sends a ASYNCHRONOUS message to all the neighbors (other
     * user/connections) in a region with additional parameters.
     * 
     * @param from
     *            The user/connection that is acting, and the only one that will
     *            NOT get the message.
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_neighbor_msg)
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     */

    public void send_neighbor_msg(User from, int noid, String op, String a1, int v1, String a2, int v2) {
        JSONLiteral msg = new_neighbor_msg(noid, op);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.finish();
        context().sendToNeighbors(from, msg);
    }

    /**
     * Sends a ASYNCHRONOUS message to all the neighbors (other
     * user/connections) in a region with additional parameters.
     *
     * @param from
     *            The user/connection that is acting, and the only one that will
     *            NOT get the message.
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_neighbor_msg)
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     */

    public void send_neighbor_msg(User from, int noid, String op, String a1, int v1, String a2, String v2) {
        JSONLiteral msg = new_neighbor_msg(noid, op);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.finish();
        context().sendToNeighbors(from, msg);
    }

    /**
     * Sends a ASYNCHRONOUS message to all the neighbors (other
     * user/connections) in a region with additional parameters.
     * 
     * @param from
     *            The user/connection that is acting, and the only one that will
     *            NOT get the message.
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_neighbor_msg)
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     * @param a3
     *            Third attribute to add
     * @param v3
     *            Third value to add
     */
    public void send_neighbor_msg(User from, int noid, String op, String a1, int v1, String a2, int v2, String a3,
            int v3) {
        JSONLiteral msg = new_neighbor_msg(noid, op);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.addParameter(a3, v3);
        msg.finish();
        context().sendToNeighbors(from, msg);
    }

    /**
     * Sends a ASYNCHRONOUS message to all the neighbors (other
     * user/connections) in a region with additional parameters.
     * 
     * @param from
     *            The user/connection that is acting, and the only one that will
     *            NOT get the message.
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_neighbor_msg)
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     * @param a3
     *            Third attribute to add
     * @param v3
     *            Third value to add
     * @param a4
     *            Fourth attribute to add
     * @param v4
     *            Fourth value to add
     */
    public void send_neighbor_msg(User from, int noid, String op, String a1, int v1, String a2, int v2, String a3,
            int v3, String a4, int v4) {
        JSONLiteral msg = new_neighbor_msg(noid, op);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.addParameter(a3, v3);
        msg.addParameter(a4, v4);
        msg.finish();
        context().sendToNeighbors(from, msg);
    }

    /**
     * Sends a ASYNCHRONOUS message to all the neighbors (other
     * user/connections) in a region with an additional string.
     * 
     * @param from
     *            The user/connection that is acting, and the only one that will
     *            NOT get the message.
     * @param noid
     *            The object that is broadcasting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_neighbor_msg)
     * @param attrib
     *            Attribute to add
     * @param value
     *            String to add
     */
    public void send_neighbor_msg(User from, int noid, String op, String attrib, String value) {
        JSONLiteral msg = new_neighbor_msg(noid, op);
        msg.addParameter(attrib, value);
        msg.finish();
        context().sendToNeighbors(from, msg);
    }

    /**
     * Create a JSONLiteral initialized with the minimum arguments to send a
     * private message to a single targeted user/connection.
     * 
     * @param noid
     *            The object that is acting.
     * @param op
     *            The STRING name of the ASYNCRONOUS request. In the PL/1 source
     *            this was a numeric constant, not a string. i.e PL/1: SPEAK$
     *            Elko: "SPEAK$". The lookup now occurs in the Client/Server
     *            bridge.
     * @return message ready to add more parameters, finish(), and send.
     */

    public JSONLiteral new_private_msg(int noid, String op) {
        JSONLiteral msg = new JSONLiteral("private", EncodeControl.forClient);
        msg.addParameter("noid", noid);
        msg.addParameter("op", op);
        return msg;
    }

    /**
     * Create a JSONLiteral initialized with the minimum arguments to send a
     * private message to a single targeted user/connection. this.noid is used
     * as the acting object.
     * 
     * @param op
     *            The STRING name of the ASYNCRONOUS request. In the PL/1 source
     *            this was a numeric constant, not a string. i.e PL/1: SPEAK$
     *            Elko: "SPEAK$". The lookup now occurs in the Client/Server
     *            bridge.
     * @return message ready to add more parameters, finish(), and send.
     */
    public JSONLiteral new_private_msg(String op) {
        return new_private_msg(this.noid, op);
    }

    /**
     * Send a private message to a specified user-connection.
     * 
     * @param from
     *            The user/connection that instigated this action. Will NOT get
     *            a copy of the message.
     * @param noid
     *            The object that is acting.
     * @param to
     *            The user/connection that is the recipient of this private
     *            message.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_private_msg)
     */
    public void send_private_msg(User from, int noid, User to, String op) {
        JSONLiteral msg = new_private_msg(noid, op);
        msg.finish();
        to.send(msg);
    }

    /**
     * Send a single-string private message to a specified user-connection.
     * 
     * @param from
     *            The user/connection that instigated this action. Will NOT get
     *            a copy of the message.
     * @param noid
     *            The object that is acting.
     * @param to
     *            The user/connection that is the recipient of this private
     *            message.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_private_msg)
     * @param text
     *            A string to send. Will be added to the message as the "text"
     *            parameter.
     */
    public void send_private_msg(User from, int noid, User to, String op, String text) {
        JSONLiteral msg = new_private_msg(noid, op);
        msg.addParameter("text", text);
        msg.finish();
        to.send(msg);
    }

    /**
     * Send a single-string private message to a specified user-connection.
     * 
     * @param from
     *            The user/connection that instigated this action. Will NOT get
     *            a copy of the message.
     * @param noid
     *            The object that is acting.
     * @param to
     *            The user/connection that is the recipient of this private
     *            message.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_private_msg)
     * @param attribute
     *            Attribute to add
     * @param value
     *            String to add
     */
    public void send_private_msg(User from, int noid, User to, String op, String attribute, String value) {
        JSONLiteral msg = new_private_msg(noid, op);
        msg.addParameter(attribute, value);
        msg.finish();
        to.send(msg);
    }

    /**
     * Send a single-byte private message to a specified user-connection.
     * 
     * @param from
     *            The user/connection that instigated this action. Will NOT get
     *            a copy of the message.
     * @param noid
     *            The object that is acting.
     * @param to
     *            The user/connection that is the recipient of this private
     *            message.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_private_msg)
     * @param attribute
     *            Attribute to add
     * @param value
     *            Value to add
     */
    public void send_private_msg(User from, int noid, User to, String op, String attribute, int value) {
        JSONLiteral msg = new_private_msg(noid, op);
        msg.addParameter(attribute, value);
        msg.finish();
        to.send(msg);
    }

    /**
     * Send a private message with additional parameters to a specified
     * user-connection.
     * 
     * @param from
     *            The user/connection that instigated this action. Will NOT get
     *            a copy of the message.
     * @param noid
     *            The object that is acting.
     * @param to
     *            The user/connection that is the recipient of this private
     *            message.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_private_msg)
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     * @param a3
     *            Third attribute to add
     * @param v3
     *            Third value to add
     * @param a4
     *            Fourth attribute to add
     * @param v4
     *            Fourth value to add
     */
    public void send_private_msg(User from, int noid, User to, String op, String a1, int v1, String a2, int v2,
            String a3, int v3, String a4, int v4) {
        JSONLiteral msg = new_private_msg(noid, op);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.addParameter(a3, v3);
        msg.addParameter(a4, v4);
        msg.finish();
        to.send(msg);
    }

    /**
     * Send a private message with additional parameters to a specified
     * user-connection.
     * 
     * @param from
     *            The user/connection that instigated this action. Will NOT get
     *            a copy of the message.
     * @param noid
     *            The object that is acting.
     * @param to
     *            The user/connection that is the recipient of this private
     *            message.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_private_msg)
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     * @param a3
     *            Third attribute to add
     * @param v3
     *            Third value to add
     */
    public void send_private_msg(User from, int noid, User to, String op, String a1, int v1, String a2, int v2,
            String a3, int v3) {
        JSONLiteral msg = new_private_msg(noid, op);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.addParameter(a3, v3);
        msg.finish();
        to.send(msg);
    }

    /**
     * Send a private message with additional parameters to a specified
     * user-connection.
     * 
     * @param from
     *            The user/connection that instigated this action. Will NOT get
     *            a copy of the message.
     * @param noid
     *            The object that is acting.
     * @param to
     *            The user/connection that is the recipient of this private
     *            message.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_private_msg)
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     */
    public void send_private_msg(User from, int noid, User to, String op, String a1, int v1, String a2, int v2) {
        JSONLiteral msg = new_private_msg(noid, op);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.finish();
        to.send(msg);
    }

    /**
     * Send a private message with additional parameters to a specified
     * user-connection.
     *
     * @param from
     *            The user/connection that instigated this action. Will NOT get
     *            a copy of the message.
     * @param noid
     *            The object that is acting.
     * @param to
     *            The user/connection that is the recipient of this private
     *            message.
     * @param op
     *            The STRING name of the ASYNCRONOUS request.
     *            (See::new_private_msg)
     * @param a1
     *            First attribute to add
     * @param v1
     *            First value to add
     * @param a2
     *            Second attribute to add
     * @param v2
     *            Second value to add
     */
    public void send_private_msg(User from, int noid, User to, String op, String a1, int v1, String a2, String v2) {
        JSONLiteral msg = new_private_msg(noid, op);
        msg.addParameter(a1, v1);
        msg.addParameter(a2, v2);
        msg.finish();
        to.send(msg);
    }

    /**
     * Clear a bit in an integer.
     * 
     * @param val
     *            starting value
     * @param bitpos
     *            1-based bit position (right to left) to clear
     * @return the starting value with the bit cleared.
     */
    public int clear_bit(int val, int bitpos) {
        return val & ~(1 << (bitpos - 1));
    }

    /**
     * Set a bit in an integer.
     * 
     * @param val
     *            starting value
     * @param bitpos
     *            1-based bit position (right to left) to set
     * @return the starting value with the bit set.
     */
    public int set_bit(int val, int bitpos) {
        return val | (1 << (bitpos - 1));
    }

    /**
     * Test to see if a specific bit is set.
     * 
     * @param val
     *            value to search
     * @param bitpos
     *            1-based bit position (right to left) to test
     * @return true if bit is set
     */
    public boolean test_bit(int val, int bitpos) {
        return val == set_bit(val, bitpos);
    }

    // TO DO: Does the outermost container need to be checkpointed?

    /**
     * Write the object to the Elko Habitat Database.
     * 
     * @param mod
     *            The Habitat Mod to checkpoint.
     */
    public void checkpoint_object(HabitatMod mod) {
        if (mod.gen_flags[MODIFIED]) {
            BasicObject object = mod.object();
            object.markAsChanged();
            object.checkpoint();
            mod.gen_flags[MODIFIED] = false;
        }
    }

    /**
     * Deletes an object from the Elko Habitat Database.
     *
     * @param mod
     *            The Habitat Mod to delete.
     */
    public void destroy_object(HabitatMod mod) {
        if (mod.noid != UNASSIGNED_NOID)
            Region.removeObjectFromRegion(mod);
        
        ((Item) mod.object()).delete();
    }

    /**
     * Is the the mod a Seating Class, requiring special handling?
     * See org.neohabitat.Seating
     * 
     * @param mod The mod being tested
     * @return true if seating.
     */
    public boolean isSeating(HabitatMod mod) {
        return (mod.HabitatClass() == CLASS_COUCH ||
                mod.HabitatClass() == CLASS_CHAIR ||
                mod.HabitatClass() == CLASS_BED);
    }  

    /**
     * Is this mod a Seating Class, requiring special handling?
     * See org.neohabitat.Seating
     * 
     * @return true if seating.
     */    
    public boolean isSeating() {
        return isSeating(this);
    }
    
    public int damage_avatar(Avatar who) {
        trace_msg("Damaging Avatar %s...", who.obj_id());
        who.health -= DAMAGE_DECREMENT;
        if (who.health <= 0) {
            // He's dead, Jim.
            trace_msg("Avatar %s has been killed", who.obj_id());
            return DEATH;
        } else {
            // Naw, he's only wounded.
            trace_msg("Avatar %s has been wounded", who.obj_id());
            return HIT;
        }
    }
    
    public int damage_avatar(Avatar who, int damage) {
        trace_msg("Damaging Avatar %s...", who.obj_id());
        who.health -= damage;
        if (who.health <= 0) {
            // He's dead, Jim.
            trace_msg("Avatar %s has been killed", who.obj_id());
            return DEATH;
        } else {
            // Naw, he's only wounded.
            trace_msg("Avatar %s has been wounded", who.obj_id());
            return HIT;
        }
    }
    
    public int damage_object(HabitatMod object) {
        if (damageable(object)) {
            destroy_object(object);
            return DESTROY;
        } else {
            return FALSE;
        }
    }
    
    public boolean damageable(HabitatMod object) {
        return object.HabitatClass() == CLASS_MAILBOX;
    }
    
    public boolean is_ranged_weapon() {
        return HabitatClass() == CLASS_GUN;
    }

    /**
     * Terminates an avatar with extreme prejudice.
     * 
     * @param victim
     *               The Avatar to terminate.
     */
    public void kill_avatar(Avatar victim) {
        for (int i = 0; i < victim.capacity() - 1; i++) {
            if (victim.contents(i) != null) {
                if (i == HANDS) {
                    victim.drop_object_in_hand();
                } else if (i != HEAD && i != MAIL_SLOT) {
                    // TODO(steve): Uncomment once object deletion works.
                    //destroy_object(victim.contents(i));
                }
            }
        }

        victim.x = 80;
        victim.y = 132;
        victim.health = 255;
        victim.bankBalance = Math.round(((float) victim.bankBalance) * 0.8f);
        victim.stun_count = 0;
        victim.set_record(HS$wealth, victim.bankBalance);
        victim.inc_record(HS$deaths);
        victim.set_record(HS$travel, 0);
        victim.set_record(HS$explored, 0);
        victim.set_record(HS$escapes, 0);
        victim.set_record(HS$teleports, 0);
        victim.set_record(HS$treasures, 0);
        victim.set_record(HS$grabs, 0);
        victim.set_record(HS$kills, 0);
        victim.set_record(HS$body_changes, 0);
        victim.set_record(HS$esp_send_count, 0);
        victim.set_record(HS$mail_send_count, 0);
        victim.set_record(HS$esp_recv_count, 0);
        victim.set_record(HS$mail_recv_count, 0);
        victim.set_record(HS$requests, 0);
        victim.gen_flags[MODIFIED] = true;

        // Penalize the victim's tokens if they are holding any.
        trace_msg("Avatar %s is transitioning on DEATH_ENTRY", obj_id());
        for (int i = 0; i < victim.capacity() - 1; i++) {
            HabitatMod curMod = victim.contents(i);
            if (curMod != null && curMod.HabitatClass() == CLASS_TOKENS) {
                Tokens token = (Tokens) curMod;
                int denom = (token.denom_lo + token.denom_hi * 256) * 50 / 100;
                trace_msg(
                    "Found tokens on DEATH_ENTRY for Avatar %s, denom_lo=%d, denom_hi=%d, denom=%d",
                    obj_id(), token.denom_lo, token.denom_hi, denom);
                token.denom_lo = denom % 256;
                token.denom_hi = (denom - token.denom_lo) / 256;
                if (denom == 0)
                    token.denom_lo = 1;
                token.gen_flags[MODIFIED] = true;
                token.checkpoint_object(token);
                trace_msg("New tokens on DEATH_ENTRY for Avatar %s, denom_lo=%d, denom_hi=%d", obj_id(),
                    token.denom_lo, token.denom_hi);
            }
        }

        // TODO Missing "SAFE TO TELEPORT" check! See Region.IsRoomForMyAvatar() FRF
        victim.change_regions(victim.turf, AUTO_TELEPORT_DIR, DEATH_ENTRY);
    }

    /**
     * Spawn a Habitat Object out of thin air.
     * 
     * @param name      The name to give the object.
     * @param mod       The Habitat Type/Elko Mod, well formed and ready to attach.
     * @param container The container that will hold the object when it arrives. null == region/context.
     * @return
     */
    public Item create_object(String name, HabitatMod mod, Container container, boolean ephemeral) {
        Item item = null;
        if (container != null) {
            item = container.object().createItem(name, true, true);
        } else {
            item = context().createItem(name, true, true);
        }

        if (item != null) {
            if (ephemeral)
                item.markAsEphemeral();
            
            mod.attachTo(item);
            mod.objectIsComplete();
            
            if (!ephemeral)
                item.checkpoint();
        }
        return item;
    }

    /**
     * Returns the SFX number (offset + id) for a provided id.
     *
     * @param sfxId The SFX ID number (like 6, or 10, etc.)
     * @return the SFX offset (128) + sfxId
     */
    public int sfx_number(int sfxId) {
        return 128 + sfxId;
    }

    /**
     * Announces a new object to a Region.
     *
     * @param obj The new HabitatMod to announce.
     */
    public void announce_object(BasicObject obj, HabitatMod container) {
        JSONLiteral itemLiteral = obj.encode(EncodeControl.forClient);
        JSONLiteral announceBroadcast = new_broadcast_msg(THE_REGION, "HEREIS_$");
        announceBroadcast.addParameter("object", itemLiteral);
        announceBroadcast.addParameter("container", container.object().ref());
        announceBroadcast.finish();
        context().send(announceBroadcast);
    }
    
    /**
     * Tell the neighbors about this object (it was sent as a reply argment to the originator)
     * 
     * @param obj
     * @param container
     */
    public void announce_object_to_neighbors(User from, BasicObject obj, HabitatMod container) {
        JSONLiteral announceNeighbors = new_neighbor_msg(THE_REGION, "HEREIS_$");
        announceNeighbors.addParameter("object", obj.encode(EncodeControl.forClient));
        announceNeighbors.addParameter("container", container.object().ref());
        announceNeighbors.finish();
        context().sendToNeighbors(from, announceNeighbors);
    }
    
    /**
     * Simulate a "make" message (instead of HEREIS_$) - used when deghosting an avatar so it
     * will appear as in a single set of objects.
     */
    
    public void fakeMakeMessage(BasicObject obj, HabitatMod container) {
        JSONLiteral itemLiteral = obj.encode(EncodeControl.forClient);
        JSONLiteral msg = new JSONLiteral(null, EncodeControl.forClient);
        msg.addParameter("to", container.obj_id());
        msg.addParameter("op", "make");
        msg.addParameter("obj", itemLiteral);
        msg.finish();
        context().send(msg);
    }
    
    public void modify_variable(User from, HabitatMod target, int offset, int new_value) {
        target.gen_flags[MODIFIED] = true;
        send_fiddle_msg(THE_REGION, target.noid, offset, new_value);
    }
    
    
    /**
     * Pays the provided amount of Tokens to the specified Avatar.
     *
     * @param who The Avatar to pay Tokens to.
     * @param amount The amount of Tokens to pay the Avatar.
     * @return TRUE or FALSE, depending upon success/failure
     */
    public static int pay_to(Avatar who, int amount) {
        if (amount < 1) {
            return FALSE;
        }
        HabitatMod handContents = who.contents(HANDS);
        Region     region       = who.current_region();
        Tokens tokens;
        if (who.empty_handed(who)) {
            tokens = new Tokens(0, 0, HANDS, 0, 0, false, 0, 0);
            Item obj = who.create_object("money", tokens, who, false);
            tokens.tset(amount);
            who.trace_msg("Created tokens in HANDS of Avatar %s: %d", who.obj_id(), tokens.tget());
            who.announce_object(obj, who);
        } else if (handContents.HabitatClass() == CLASS_TOKENS) {
            tokens = (Tokens) handContents;
            amount += tokens.tget();
            if (amount > 65535) {
                who.trace_msg("Tokens for Avatar %s > 65535: %d", who.obj_id(), amount);
                return FALSE;
            }
            who.trace_msg("Updated tokens in HANDS of Avatar %s: %d", who.obj_id(), tokens.tget());
            tokens.tset(amount);
        } else {
            tokens = new Tokens(0, who.x-4, who.y, 0, 0, false, 0, 0);
            who.trace_msg("Attempting to create tokens in region %s for Avatar %s: %d", region.obj_id(),
                who.obj_id(), tokens.tget());
            Item item = who.create_object("money", tokens, region, false);
            if (item == null) {
                who.trace_msg("FAILED to create tokens in region %s for Avatar %s", region.obj_id(),
                    who.obj_id());
                return FALSE;
            }
            tokens.tset(amount);
            who.announce_object(item, region);
        }
        tokens.gen_flags[MODIFIED] = true;
        return TRUE;
    }

    /**
     * Converts a Unicode string to an int[] of Commodore-compliant PETSCII,
     * bounded by the provided maximum length.
     *
     * @param text the Unicode string to encode
     * @param maxLength the maximum length of the PETSCII array
     * @return an int[] containing the Commodore-compliant PETSCII
     */
    public static int[] convert_to_petscii(String text, int maxLength) {
        int petsciiLength = text.length();
        if (petsciiLength > maxLength) {
            petsciiLength = maxLength;
        }
        int[] petscii = new int[petsciiLength];
        for (int c = 0; c < petsciiLength; c++) {
            petscii[c] = (int) text.charAt(c) & 0xff;
        }
        return petscii;
    }

    /**
     * Bounds an int[] by a provided maximum length.
     *
     * @param intArray an int[] to bound by the provided maximum length
     * @param maxLength the maximum length of the int[]
     * @return an int[] bounded by the provided maximum length
     */
    public static int[] bound_int_array(int[] intArray, int maxLength) {
        if (intArray.length > maxLength) {
            int[] boundedAscii = new int[maxLength];
            System.arraycopy(intArray, 0, boundedAscii, 0, maxLength);
            return boundedAscii;
        } else {
            return intArray;
        }
    }

    /**
     * Returns the title page of a Document-like object.
     *
     * @param title The title of the Document-like object
     * @param use_flag How the Document-like object is being used
     * @return String containing the title page.
     */
    public static String get_title_page(String title, int use_flag) {
        String nice_title = title.trim();
        switch (use_flag) {
            case BOOK$HELP:
                if (nice_title.length() == 0)
                    return "This book is untitled.";
                return String.format("This book is '%s'.", nice_title);
            case BOOK$VENDO:
                if (nice_title.length() == 0)
                    return "This book is untitled.";
                return String.format("This is '%s'.", nice_title);
            case PAPER$HELP:
                if (nice_title.length() != 0) {
                    return "This paper begins \"" + nice_title + "\".";
                }
        }
        return "";
    }

    /**
     * Concatenates multiple int[] in the order provided.
     *
     * @param first first int[] to concatenate
     * @param rest remaining int[]s to concatenate
     * @return an int[] containing the provided concatenated arrays
     */
    public static int[] concat_int_arrays(int[] first, int[]... rest) {
        // Determines the maximum length of the concatenated array.
        int totalLength = first.length;
        for (int[] nextArray : rest) {
            totalLength += nextArray.length;
        }

        // Concatenates all int[] arrays with System.arraycopy.
        int[] concatArray = new int[totalLength];
        System.arraycopy(first, 0, concatArray, 0, first.length);
        int curStartPoint = first.length;
        for (int[] nextArray : rest) {
            System.arraycopy(nextArray, 0, concatArray, curStartPoint, nextArray.length);
            curStartPoint += nextArray.length;
        }
        return concatArray;
    }

    /**
     * Concatenates multiple arrays in the provided order, borrowed from:
     * http://stackoverflow.com/posts/784842/revisions
     *
     * @param first first array to concatenate
     * @param rest remaining arrays to concatenate
     * @param <T> type of array
     * @return concatenated array
     */
    public static <T> T[] concat_arrays(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * Returns the text of a Throwable as a String.
     *
     * @param t a Java Throwable
     * @return String-formatted traceback from Throwable
     */
    public String getTracebackString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Converts a Java String to an int[] expected by all ASCII-returning
     * methods.
     *
     * @param s a Java String
     * @return an int[] representation of the provided String
     */
    public int[] stringToIntArray(String s) {
        int[] asInts = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            asInts[i] = s.charAt(i);
        }
        return asInts;
    }

}
