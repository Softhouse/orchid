package se.softhouse.garden.orchid.vaadin.widgetset.client.ui;

/**
 * Copyright (c) 2012, Ermin Hrkalovic, Softhouse Consulting AB
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import se.softhouse.garden.orchid.vaadin.widgetset.client.ui.VOrchidScrollTable.VScrollTableBody.VScrollTableRow;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Focusable;
import com.vaadin.terminal.gwt.client.MouseEventDetails;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.TooltipInfo;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.VTooltip;
import com.vaadin.terminal.gwt.client.ui.Action;
import com.vaadin.terminal.gwt.client.ui.ActionOwner;
import com.vaadin.terminal.gwt.client.ui.FocusableScrollPanel;
import com.vaadin.terminal.gwt.client.ui.Table;
import com.vaadin.terminal.gwt.client.ui.TouchScrollDelegate;
import com.vaadin.terminal.gwt.client.ui.TreeAction;
import com.vaadin.terminal.gwt.client.ui.VEmbedded;
import com.vaadin.terminal.gwt.client.ui.VLabel;
import com.vaadin.terminal.gwt.client.ui.VTextField;
import com.vaadin.terminal.gwt.client.ui.dd.DDUtil;
import com.vaadin.terminal.gwt.client.ui.dd.VAbstractDropHandler;
import com.vaadin.terminal.gwt.client.ui.dd.VAcceptCallback;
import com.vaadin.terminal.gwt.client.ui.dd.VDragAndDropManager;
import com.vaadin.terminal.gwt.client.ui.dd.VDragEvent;
import com.vaadin.terminal.gwt.client.ui.dd.VHasDropHandler;
import com.vaadin.terminal.gwt.client.ui.dd.VTransferable;
import com.vaadin.terminal.gwt.client.ui.dd.VerticalDropLocation;

//import com.vaadin.terminal.gwt.client.ui.VScrollTable.VScrollTableBody.VScrollTableRow;

/**
 * VScrollTable
 * 
 * VScrollTable is a FlowPanel having two widgets in it: * TableHead component *
 * ScrollPanel
 * 
 * TableHead contains table's header and widgets + logic for resizing,
 * reordering and hiding columns.
 * 
 * ScrollPanel contains VScrollTableBody object which handles content. To save
 * some bandwidth and to improve clients responsiveness with loads of data, in
 * VScrollTableBody all rows are not necessary rendered. There are "spacers" in
 * VScrollTableBody to use the exact same space as non-rendered rows would use.
 * This way we can use seamlessly traditional scrollbars and scrolling to fetch
 * more rows instead of "paging".
 * 
 * In VScrollTable we listen to scroll events. On horizontal scrolling we also
 * update TableHeads scroll position which has its scrollbars hidden. On
 * vertical scroll events we will check if we are reaching the end of area where
 * we have rows rendered and
 * 
 * TODO implement unregistering for child components in Cells
 */
public class VOrchidScrollTable extends FlowPanel implements Table, ScrollHandler, VHasDropHandler, FocusHandler, BlurHandler, Focusable, ActionOwner {

	public static final String ATTRIBUTE_PAGEBUFFER_FIRST = "pb-ft";
	public static final String ATTRIBUTE_PAGEBUFFER_LAST = "pb-l";

	/**
	 * Tell the client that old keys are no longer valid because the server has
	 * cleared its key map.
	 */
	public static final String ATTRIBUTE_KEY_MAPPER_RESET = "clearKeyMap";

	private static final String ROW_HEADER_COLUMN_KEY = "0";

	public static final String CLASSNAME = "v-table";
	public static final String CLASSNAME_SELECTION_FOCUS = CLASSNAME + "-focus";

	public static final String ITEM_CLICK_EVENT_ID = "itemClick";
	public static final String HEADER_CLICK_EVENT_ID = "handleHeaderClick";
	public static final String FOOTER_CLICK_EVENT_ID = "handleFooterClick";
	public static final String COLUMN_RESIZE_EVENT_ID = "columnResize";
	public static final String COLUMN_REORDER_EVENT_ID = "columnReorder";

	private static final double CACHE_RATE_DEFAULT = 2;

	/**
	 * The default multi select mode where simple left clicks only selects one
	 * item, CTRL+left click selects multiple items and SHIFT-left click selects
	 * a range of items.
	 */
	private static final int MULTISELECT_MODE_DEFAULT = 0;

	/**
	 * The simple multiselect mode is what the table used to have before
	 * ctrl/shift selections were added. That is that when this is set clicking
	 * on an item selects/deselects the item and no ctrl/shift selections are
	 * available.
	 */
	private static final int MULTISELECT_MODE_SIMPLE = 1;

	/**
	 * multiple of pagelength which component will cache when requesting more
	 * rows
	 */
	private double cache_rate = CACHE_RATE_DEFAULT;
	/**
	 * fraction of pageLenght which can be scrolled without making new request
	 */
	private double cache_react_rate = 0.75 * this.cache_rate;

	public static final char ALIGN_CENTER = 'c';
	public static final char ALIGN_LEFT = 'b';
	public static final char ALIGN_RIGHT = 'e';
	private static final int CHARCODE_SPACE = 32;
	private int firstRowInViewPort = 0;
	private int pageLength = 15;
	private int lastRequestedFirstvisible = 0; // to detect "serverside scroll"

	protected boolean showRowHeaders = false;

	private String[] columnOrder;

	protected ApplicationConnection client;
	protected String paintableId;

	private boolean immediate;
	private boolean nullSelectionAllowed = true;

	private int selectMode = Table.SELECT_MODE_NONE;

	private final HashSet<String> selectedRowKeys = new HashSet<String>();

	/*
	 * When scrolling and selecting at the same time, the selections are not in
	 * sync with the server while retrieving new rows (until key is released).
	 */
	private HashSet<Object> unSyncedselectionsBeforeRowFetch;

	/*
	 * These are used when jumping between pages when pressing Home and End
	 */
	private boolean selectLastItemInNextRender = false;
	private boolean selectFirstItemInNextRender = false;
	private boolean focusFirstItemInNextRender = false;
	private boolean focusLastItemInNextRender = false;

	/*
	 * The currently focused row
	 */
	private VScrollTableRow focusedRow;

	/*
	 * Helper to store selection range start in when using the keyboard
	 */
	private VScrollTableRow selectionRangeStart;

	/*
	 * Flag for notifying when the selection has changed and should be sent to
	 * the server
	 */
	private boolean selectionChanged = false;

	/*
	 * The speed (in pixels) which the scrolling scrolls vertically/horizontally
	 */
	private int scrollingVelocity = 10;

	private Timer scrollingVelocityTimer = null;

	private String[] bodyActionKeys;

	private final boolean enableDebug = false;

	private static final boolean hasNativeTouchScrolling = BrowserInfo.get().isTouchDevice() && !BrowserInfo.get().requiresTouchScrollDelegate();

	private Set<String> noncollapsibleColumns;

	/**
	 * The last known row height used to preserve the height of a table with
	 * custom row heights and a fixed page length after removing the last row
	 * from the table.
	 * 
	 * A new VScrollTableBody instance is created every time the number of rows
	 * changes causing {@link VScrollTableBody#rowHeight} to be discarded and
	 * the height recalculated by {@link VScrollTableBody#getRowHeight(boolean)}
	 * to avoid some rounding problems, e.g. round(2 * 19.8) / 2 = 20 but
	 * round(3 * 19.8) / 3 = 19.66.
	 */
	private double lastKnownRowHeight = Double.NaN;

	/**
	 * Represents a select range of rows
	 */
	private class SelectionRange {
		private VScrollTableRow startRow;
		private final int length;

		/**
		 * Constuctor.
		 */
		public SelectionRange(VScrollTableRow row1, VScrollTableRow row2) {
			VScrollTableRow endRow;
			if (row2.isBefore(row1)) {
				this.startRow = row2;
				endRow = row1;
			} else {
				this.startRow = row1;
				endRow = row2;
			}
			this.length = endRow.getIndex() - this.startRow.getIndex() + 1;
		}

		public SelectionRange(VScrollTableRow row, int length) {
			this.startRow = row;
			this.length = length;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.startRow.getKey() + "-" + this.length;
		}

		private boolean inRange(VScrollTableRow row) {
			return row.getIndex() >= this.startRow.getIndex() && row.getIndex() < this.startRow.getIndex() + this.length;
		}

		public Collection<SelectionRange> split(VScrollTableRow row) {
			assert row.isAttached();
			ArrayList<SelectionRange> ranges = new ArrayList<SelectionRange>(2);

			int endOfFirstRange = row.getIndex() - 1;
			if (!(endOfFirstRange - this.startRow.getIndex() < 0)) {
				// create range of first part unless its length is < 1
				ranges.add(new SelectionRange(this.startRow, endOfFirstRange - this.startRow.getIndex() + 1));
			}
			int startOfSecondRange = row.getIndex() + 1;
			if (!(getEndIndex() - startOfSecondRange < 0)) {
				// create range of second part unless its length is < 1
				VScrollTableRow startOfRange = VOrchidScrollTable.this.scrollBody.getRowByRowIndex(startOfSecondRange);
				ranges.add(new SelectionRange(startOfRange, getEndIndex() - startOfSecondRange + 1));
			}
			return ranges;
		}

		private int getEndIndex() {
			return this.startRow.getIndex() + this.length - 1;
		}

	};

	private final HashSet<SelectionRange> selectedRowRanges = new HashSet<SelectionRange>();

	private boolean initializedAndAttached = false;

	/**
	 * Flag to indicate if a column width recalculation is needed due update.
	 */
	private boolean headerChangedDuringUpdate = false;

	protected final TableHead tHead = new TableHead();

	private final TableFooter tFoot = new TableFooter();

	private final FocusableScrollPanel scrollBodyPanel = new FocusableScrollPanel(true);

	private final KeyPressHandler navKeyPressHandler = new KeyPressHandler() {
		@Override
		public void onKeyPress(KeyPressEvent keyPressEvent) {
			// This is used for Firefox only, since Firefox auto-repeat
			// works correctly only if we use a key press handler, other
			// browsers handle it correctly when using a key down handler
			if (!BrowserInfo.get().isGecko()) {
				return;
			}

			NativeEvent event = keyPressEvent.getNativeEvent();
			if (!VOrchidScrollTable.this.enabled) {
				// Cancel default keyboard events on a disabled Table
				// (prevents scrolling)
				event.preventDefault();
			} else if (VOrchidScrollTable.this.hasFocus) {
				// Key code in Firefox/onKeyPress is present only for
				// special keys, otherwise 0 is returned
				int keyCode = event.getKeyCode();
				if (keyCode == 0 && event.getCharCode() == ' ') {
					// Provide a keyCode for space to be compatible with
					// FireFox keypress event
					keyCode = CHARCODE_SPACE;
				}

				if (handleNavigation(keyCode, event.getCtrlKey() || event.getMetaKey(), event.getShiftKey())) {
					event.preventDefault();
				}

				startScrollingVelocityTimer();
			}
		}

	};

	private final KeyUpHandler navKeyUpHandler = new KeyUpHandler() {

		@Override
		public void onKeyUp(KeyUpEvent keyUpEvent) {
			NativeEvent event = keyUpEvent.getNativeEvent();
			int keyCode = event.getKeyCode();

			if (!isFocusable()) {
				cancelScrollingVelocityTimer();
			} else if (isNavigationKey(keyCode)) {
				if (keyCode == getNavigationDownKey() || keyCode == getNavigationUpKey()) {
					/*
					 * in multiselect mode the server may still have value from
					 * previous page. Clear it unless doing multiselection or
					 * just moving focus.
					 */
					if (!event.getShiftKey() && !event.getCtrlKey()) {
						instructServerToForgetPreviousSelections();
					}
					sendSelectedRows();
				}
				cancelScrollingVelocityTimer();
				VOrchidScrollTable.this.navKeyDown = false;
			}
		}
	};

	private final KeyDownHandler navKeyDownHandler = new KeyDownHandler() {

		@Override
		public void onKeyDown(KeyDownEvent keyDownEvent) {
			NativeEvent event = keyDownEvent.getNativeEvent();
			// This is not used for Firefox
			if (BrowserInfo.get().isGecko()) {
				return;
			}

			if (!VOrchidScrollTable.this.enabled) {
				// Cancel default keyboard events on a disabled Table
				// (prevents scrolling)
				event.preventDefault();
			} else if (VOrchidScrollTable.this.hasFocus) {
				if (handleNavigation(event.getKeyCode(), event.getCtrlKey() || event.getMetaKey(), event.getShiftKey())) {
					VOrchidScrollTable.this.navKeyDown = true;
					event.preventDefault();
				}

				startScrollingVelocityTimer();
			}
		}
	};
	private int totalRows;

	private Set<String> collapsedColumns;

	private final RowRequestHandler rowRequestHandler;
	private VScrollTableBody scrollBody;
	private int firstvisible = 0;
	private boolean sortAscending;
	private String sortColumn;
	private String oldSortColumn;
	private boolean columnReordering;

	/**
	 * This map contains captions and icon urls for actions like: * "33_c" ->
	 * "Edit" * "33_i" -> "http://dom.com/edit.png"
	 */
	private final HashMap<Object, String> actionMap = new HashMap<Object, String>();
	private String[] visibleColOrder;
	private boolean initialContentReceived = false;
	private Element scrollPositionElement;
	private boolean enabled;
	private boolean showColHeaders;
	private boolean showColFooters;

	/** flag to indicate that table body has changed */
	private boolean isNewBody = true;

	/*
	 * Read from the "recalcWidths" -attribute. When it is true, the table will
	 * recalculate the widths for columns - desirable in some cases. For #1983,
	 * marked experimental.
	 */
	boolean recalcWidths = false;

	private final ArrayList<Panel> lazyUnregistryBag = new ArrayList<Panel>();
	private String height;
	private String width = "";
	private boolean rendering = false;
	private boolean hasFocus = false;
	private int dragmode;

	private int multiselectmode;
	private int tabIndex;
	private TouchScrollDelegate touchScrollDelegate;

	private int lastRenderedHeight;

	/**
	 * Values (serverCacheFirst+serverCacheLast) sent by server that tells which
	 * rows (indexes) are in the server side cache (page buffer). -1 means
	 * unknown. The server side cache row MUST MATCH the client side cache rows.
	 * 
	 * If the client side cache contains additional rows with e.g. buttons, it
	 * will cause out of sync when such a button is pressed.
	 * 
	 * If the server side cache contains additional rows with e.g. buttons,
	 * scrolling in the client will cause empty buttons to be rendered
	 * (cached=true request for non-existing components)
	 */
	private int serverCacheFirst = -1;
	private int serverCacheLast = -1;

	/**
	 * Used to recall the position of an open context menu if we need to close
	 * and reopen it during a row update.
	 */
	private class ContextMenuDetails {
		String rowKey;
		int left;
		int top;

		ContextMenuDetails(String rowKey, int left, int top) {
			this.rowKey = rowKey;
			this.left = left;
			this.top = top;
		}
	}

	ContextMenuDetails contextMenu;

	public VOrchidScrollTable() {
		setMultiSelectMode(MULTISELECT_MODE_DEFAULT);

		this.scrollBodyPanel.addStyleName(CLASSNAME + "-body-wrapper");
		this.scrollBodyPanel.addFocusHandler(this);
		this.scrollBodyPanel.addBlurHandler(this);

		this.scrollBodyPanel.addScrollHandler(this);
		this.scrollBodyPanel.addStyleName(CLASSNAME + "-body");

		/*
		 * Firefox auto-repeat works correctly only if we use a key press
		 * handler, other browsers handle it correctly when using a key down
		 * handler
		 */
		if (BrowserInfo.get().isGecko()) {
			this.scrollBodyPanel.addKeyPressHandler(this.navKeyPressHandler);
		} else {
			this.scrollBodyPanel.addKeyDownHandler(this.navKeyDownHandler);
		}
		this.scrollBodyPanel.addKeyUpHandler(this.navKeyUpHandler);

		this.scrollBodyPanel.sinkEvents(Event.TOUCHEVENTS);

		this.scrollBodyPanel.sinkEvents(Event.ONCONTEXTMENU);
		this.scrollBodyPanel.addDomHandler(new ContextMenuHandler() {
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				handleBodyContextMenu(event);
			}
		}, ContextMenuEvent.getType());

		setStyleName(CLASSNAME);

		add(this.tHead);
		add(this.scrollBodyPanel);
		add(this.tFoot);

		this.rowRequestHandler = new RowRequestHandler();
	}

	private void handleBodyContextMenu(ContextMenuEvent event) {
		if (this.enabled && this.bodyActionKeys != null) {
			int left = Util.getTouchOrMouseClientX(event.getNativeEvent());
			int top = Util.getTouchOrMouseClientY(event.getNativeEvent());
			top += Window.getScrollTop();
			left += Window.getScrollLeft();
			this.client.getContextMenu().showAt(this, left, top);

			// Only prevent browser context menu if there are action handlers
			// registered
			event.stopPropagation();
			event.preventDefault();
		}
	}

	/**
	 * Fires a column resize event which sends the resize information to the
	 * server.
	 * 
	 * @param columnId
	 *            The columnId of the column which was resized
	 * @param originalWidth
	 *            The width in pixels of the column before the resize event
	 * @param newWidth
	 *            The width in pixels of the column after the resize event
	 */
	private void fireColumnResizeEvent(String columnId, int originalWidth, int newWidth) {
		this.client.updateVariable(this.paintableId, "columnResizeEventColumn", columnId, false);
		this.client.updateVariable(this.paintableId, "columnResizeEventPrev", originalWidth, false);
		this.client.updateVariable(this.paintableId, "columnResizeEventCurr", newWidth, this.immediate);

	}

	/**
	 * Non-immediate variable update of column widths for a collection of
	 * columns.
	 * 
	 * @param columns
	 *            the columns to trigger the events for.
	 */
	private void sendColumnWidthUpdates(Collection<HeaderCell> columns) {
		String[] newSizes = new String[columns.size()];
		int ix = 0;
		for (HeaderCell cell : columns) {
			newSizes[ix++] = cell.getColKey() + ":" + cell.getWidth();
		}
		this.client.updateVariable(this.paintableId, "columnWidthUpdates", newSizes, false);
	}

	/**
	 * Moves the focus one step down
	 * 
	 * @return Returns true if succeeded
	 */
	private boolean moveFocusDown() {
		return moveFocusDown(0);
	}

	/**
	 * Moves the focus down by 1+offset rows
	 * 
	 * @return Returns true if succeeded, else false if the selection could not
	 *         be move downwards
	 */
	private boolean moveFocusDown(int offset) {
		if (isSelectable()) {
			if (this.focusedRow == null && this.scrollBody.iterator().hasNext()) {
				// FIXME should focus first visible from top, not first rendered
				// ??
				return setRowFocus((VScrollTableRow) this.scrollBody.iterator().next());
			} else {
				VScrollTableRow next = getNextRow(this.focusedRow, offset);
				if (next != null) {
					return setRowFocus(next);
				}
			}
		}

		return false;
	}

	/**
	 * Moves the selection one step up
	 * 
	 * @return Returns true if succeeded
	 */
	private boolean moveFocusUp() {
		return moveFocusUp(0);
	}

	/**
	 * Moves the focus row upwards
	 * 
	 * @return Returns true if succeeded, else false if the selection could not
	 *         be move upwards
	 * 
	 */
	private boolean moveFocusUp(int offset) {
		if (isSelectable()) {
			if (this.focusedRow == null && this.scrollBody.iterator().hasNext()) {
				// FIXME logic is exactly the same as in moveFocusDown, should
				// be the opposite??
				return setRowFocus((VScrollTableRow) this.scrollBody.iterator().next());
			} else {
				VScrollTableRow prev = getPreviousRow(this.focusedRow, offset);
				if (prev != null) {
					return setRowFocus(prev);
				} else {
					VConsole.log("no previous available");
				}
			}
		}

		return false;
	}

	/**
	 * Selects a row where the current selection head is
	 * 
	 * @param ctrlSelect
	 *            Is the selection a ctrl+selection
	 * @param shiftSelect
	 *            Is the selection a shift+selection
	 * @return Returns truw
	 */
	private void selectFocusedRow(boolean ctrlSelect, boolean shiftSelect) {
		if (this.focusedRow != null) {
			// Arrows moves the selection and clears previous selections
			if (isSelectable() && !ctrlSelect && !shiftSelect) {
				deselectAll();
				this.focusedRow.toggleSelection();
				this.selectionRangeStart = this.focusedRow;
			} else if (isSelectable() && ctrlSelect && !shiftSelect) {
				// Ctrl+arrows moves selection head
				this.selectionRangeStart = this.focusedRow;
				// No selection, only selection head is moved
			} else if (isMultiSelectModeAny() && !ctrlSelect && shiftSelect) {
				// Shift+arrows selection selects a range
				this.focusedRow.toggleShiftSelection(shiftSelect);
			}
		}
	}

	/**
	 * Sends the selection to the server if changed since the last update/visit.
	 */
	protected void sendSelectedRows() {
		sendSelectedRows(this.immediate);
	}

	/**
	 * Sends the selection to the server if it has been changed since the last
	 * update/visit.
	 * 
	 * @param immediately
	 *            set to true to immediately send the rows
	 */
	protected void sendSelectedRows(boolean immediately) {
		// Don't send anything if selection has not changed
		if (!this.selectionChanged) {
			return;
		}

		// Reset selection changed flag
		this.selectionChanged = false;

		// Note: changing the immediateness of this might require changes to
		// "clickEvent" immediateness also.
		if (isMultiSelectModeDefault()) {
			// Convert ranges to a set of strings
			Set<String> ranges = new HashSet<String>();
			for (SelectionRange range : this.selectedRowRanges) {
				ranges.add(range.toString());
			}

			// Send the selected row ranges
			this.client.updateVariable(this.paintableId, "selectedRanges", ranges.toArray(new String[this.selectedRowRanges.size()]), false);

			// clean selectedRowKeys so that they don't contain excess values
			for (Iterator<String> iterator = this.selectedRowKeys.iterator(); iterator.hasNext();) {
				String key = iterator.next();
				VScrollTableRow renderedRowByKey = getRenderedRowByKey(key);
				if (renderedRowByKey != null) {
					for (SelectionRange range : this.selectedRowRanges) {
						if (range.inRange(renderedRowByKey)) {
							iterator.remove();
						}
					}
				} else {
					// orphaned selected key, must be in a range, ignore
					iterator.remove();
				}

			}
		}

		// Send the selected rows
		this.client.updateVariable(this.paintableId, "selected", this.selectedRowKeys.toArray(new String[this.selectedRowKeys.size()]), immediately);

	}

	/**
	 * Get the key that moves the selection head upwards. By default it is the
	 * up arrow key but by overriding this you can change the key to whatever
	 * you want.
	 * 
	 * @return The keycode of the key
	 */
	protected int getNavigationUpKey() {
		return KeyCodes.KEY_UP;
	}

	/**
	 * Get the key that moves the selection head downwards. By default it is the
	 * down arrow key but by overriding this you can change the key to whatever
	 * you want.
	 * 
	 * @return The keycode of the key
	 */
	protected int getNavigationDownKey() {
		return KeyCodes.KEY_DOWN;
	}

	/**
	 * Get the key that scrolls to the left in the table. By default it is the
	 * left arrow key but by overriding this you can change the key to whatever
	 * you want.
	 * 
	 * @return The keycode of the key
	 */
	protected int getNavigationLeftKey() {
		return KeyCodes.KEY_LEFT;
	}

	/**
	 * Get the key that scroll to the right on the table. By default it is the
	 * right arrow key but by overriding this you can change the key to whatever
	 * you want.
	 * 
	 * @return The keycode of the key
	 */
	protected int getNavigationRightKey() {
		return KeyCodes.KEY_RIGHT;
	}

	/**
	 * Get the key that selects an item in the table. By default it is the space
	 * bar key but by overriding this you can change the key to whatever you
	 * want.
	 * 
	 * @return
	 */
	protected int getNavigationSelectKey() {
		return CHARCODE_SPACE;
	}

	/**
	 * Get the key the moves the selection one page up in the table. By default
	 * this is the Page Up key but by overriding this you can change the key to
	 * whatever you want.
	 * 
	 * @return
	 */
	protected int getNavigationPageUpKey() {
		return KeyCodes.KEY_PAGEUP;
	}

	/**
	 * Get the key the moves the selection one page down in the table. By
	 * default this is the Page Down key but by overriding this you can change
	 * the key to whatever you want.
	 * 
	 * @return
	 */
	protected int getNavigationPageDownKey() {
		return KeyCodes.KEY_PAGEDOWN;
	}

	/**
	 * Get the key the moves the selection to the beginning of the table. By
	 * default this is the Home key but by overriding this you can change the
	 * key to whatever you want.
	 * 
	 * @return
	 */
	protected int getNavigationStartKey() {
		return KeyCodes.KEY_HOME;
	}

	/**
	 * Get the key the moves the selection to the end of the table. By default
	 * this is the End key but by overriding this you can change the key to
	 * whatever you want.
	 * 
	 * @return
	 */
	protected int getNavigationEndKey() {
		return KeyCodes.KEY_END;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.vaadin.terminal.gwt.client.Paintable#updateFromUIDL(com.vaadin.terminal
	 * .gwt.client.UIDL, com.vaadin.terminal.gwt.client.ApplicationConnection)
	 */
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		this.rendering = true;

		// On the first rendering, add a handler to clear saved context menu
		// details when the menu closes. See #8526.
		if (this.client == null) {
			client.getContextMenu().addCloseHandler(new CloseHandler<PopupPanel>() {
				@Override
				public void onClose(CloseEvent<PopupPanel> event) {
					VOrchidScrollTable.this.contextMenu = null;
				}
			});
		}
		// If a row has an open context menu, it will be closed as the row is
		// detached. Retain a reference here so we can restore the menu if
		// required.
		ContextMenuDetails savedContextMenu = this.contextMenu;

		if (uidl.hasAttribute(ATTRIBUTE_PAGEBUFFER_FIRST)) {
			this.serverCacheFirst = uidl.getIntAttribute(ATTRIBUTE_PAGEBUFFER_FIRST);
			this.serverCacheLast = uidl.getIntAttribute(ATTRIBUTE_PAGEBUFFER_LAST);
		} else {
			this.serverCacheFirst = -1;
			this.serverCacheLast = -1;
		}
		/*
		 * We need to do this before updateComponent since updateComponent calls
		 * this.setHeight() which will calculate a new body height depending on
		 * the space available.
		 */
		if (uidl.hasAttribute("colfooters")) {
			this.showColFooters = uidl.getBooleanAttribute("colfooters");
		}

		this.tFoot.setVisible(this.showColFooters);

		if (client.updateComponent(this, uidl, true)) {
			this.rendering = false;
			return;
		}

		this.enabled = !uidl.hasAttribute("disabled");

		if (BrowserInfo.get().isIE8() && !this.enabled) {
			/*
			 * The disabled shim will not cover the table body if it is relative
			 * in IE8. See #7324
			 */
			this.scrollBodyPanel.getElement().getStyle().setPosition(Position.STATIC);
		} else if (BrowserInfo.get().isIE8()) {
			this.scrollBodyPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		}

		this.client = client;
		this.paintableId = uidl.getStringAttribute("id");
		this.immediate = uidl.getBooleanAttribute("immediate");

		int previousTotalRows = this.totalRows;
		updateTotalRows(uidl);
		boolean totalRowsChanged = (this.totalRows != previousTotalRows);

		updateDragMode(uidl);

		updateSelectionProperties(uidl);

		if (uidl.hasAttribute("alb")) {
			this.bodyActionKeys = uidl.getStringArrayAttribute("alb");
		} else {
			// Need to clear the actions if the action handlers have been
			// removed
			this.bodyActionKeys = null;
		}

		setCacheRateFromUIDL(uidl);

		this.recalcWidths = uidl.hasAttribute("recalcWidths");
		if (this.recalcWidths) {
			this.tHead.clear();
			this.tFoot.clear();
		}

		updatePageLength(uidl);

		updateFirstVisibleAndScrollIfNeeded(uidl);

		this.showRowHeaders = uidl.getBooleanAttribute("rowheaders");
		this.showColHeaders = uidl.getBooleanAttribute("colheaders");

		updateSortingProperties(uidl);

		boolean keyboardSelectionOverRowFetchInProgress = selectSelectedRows(uidl);

		updateActionMap(uidl);

		updateColumnProperties(uidl);

		UIDL ac = uidl.getChildByTagName("-ac");
		if (ac == null) {
			if (this.dropHandler != null) {
				// remove dropHandler if not present anymore
				this.dropHandler = null;
			}
		} else {
			if (this.dropHandler == null) {
				this.dropHandler = new VScrollTableDropHandler();
			}
			this.dropHandler.updateAcceptRules(ac);
		}

		UIDL partialRowAdditions = uidl.getChildByTagName("prows");
		UIDL partialRowUpdates = uidl.getChildByTagName("urows");
		if (partialRowUpdates != null || partialRowAdditions != null) {
			// we may have pending cache row fetch, cancel it. See #2136
			this.rowRequestHandler.cancel();

			updateRowsInBody(partialRowUpdates);
			addAndRemoveRows(partialRowAdditions);
		} else {
			UIDL rowData = uidl.getChildByTagName("rows");
			if (rowData != null) {
				// we may have pending cache row fetch, cancel it. See #2136
				this.rowRequestHandler.cancel();

				if (!this.recalcWidths && this.initializedAndAttached) {
					updateBody(rowData, uidl.getIntAttribute("firstrow"), uidl.getIntAttribute("rows"));
					if (this.headerChangedDuringUpdate) {
						triggerLazyColumnAdjustment(true);
					} else if (!isScrollPositionVisible() || totalRowsChanged || this.lastRenderedHeight != this.scrollBody.getOffsetHeight()) {
						// webkits may still bug with their disturbing scrollbar
						// bug, see #3457
						// Run overflow fix for the scrollable area
						// #6698 - If there's a scroll going on, don't abort it
						// by changing overflows as the length of the contents
						// *shouldn't* have changed (unless the number of rows
						// or the height of the widget has also changed)
						Scheduler.get().scheduleDeferred(new Command() {
							@Override
							public void execute() {
								Util.runWebkitOverflowAutoFix(VOrchidScrollTable.this.scrollBodyPanel.getElement());
							}
						});
					}
				} else {
					initializeRows(uidl, rowData);
				}
			}
		}

		// If a row had an open context menu before the update, and after the
		// update there's a row with the same key as that row, restore the
		// context menu. See #8526.
		if (this.enabled && savedContextMenu != null) {
			for (Widget w : this.scrollBody.renderedRows) {
				VScrollTableRow row = (VScrollTableRow) w;
				if (row.isVisible() && row.getKey().equals(savedContextMenu.rowKey)) {
					this.contextMenu = savedContextMenu;
					client.getContextMenu().showAt(row, savedContextMenu.left, savedContextMenu.top);
				}
			}
		}

		if (!isSelectable()) {
			this.scrollBody.addStyleName(CLASSNAME + "-body-noselection");
		} else {
			this.scrollBody.removeStyleName(CLASSNAME + "-body-noselection");
		}

		hideScrollPositionAnnotation();
		purgeUnregistryBag();

		// selection is no in sync with server, avoid excessive server visits by
		// clearing to flag used during the normal operation
		if (!keyboardSelectionOverRowFetchInProgress) {
			this.selectionChanged = false;
		}

		/*
		 * This is called when the Home or page up button has been pressed in
		 * selectable mode and the next selected row was not yet rendered in the
		 * client
		 */
		if (this.selectFirstItemInNextRender || this.focusFirstItemInNextRender) {
			selectFirstRenderedRowInViewPort(this.focusFirstItemInNextRender);
			this.selectFirstItemInNextRender = this.focusFirstItemInNextRender = false;
		}

		/*
		 * This is called when the page down or end button has been pressed in
		 * selectable mode and the next selected row was not yet rendered in the
		 * client
		 */
		if (this.selectLastItemInNextRender || this.focusLastItemInNextRender) {
			selectLastRenderedRowInViewPort(this.focusLastItemInNextRender);
			this.selectLastItemInNextRender = this.focusLastItemInNextRender = false;
		}
		this.multiselectPending = false;

		if (this.focusedRow != null) {
			if (!this.focusedRow.isAttached() && !this.rowRequestHandler.isRunning()) {
				// focused row has been orphaned, can't focus
				focusRowFromBody();
			}
		}

		/*
		 * If the server has (re)initialized the rows, our selectionRangeStart
		 * row will point to an index that the server knows nothing about,
		 * causing problems if doing multi selection with shift. The field will
		 * be cleared a little later when the row focus has been restored.
		 * (#8584)
		 */
		if (uidl.hasAttribute(ATTRIBUTE_KEY_MAPPER_RESET) && uidl.getBooleanAttribute(ATTRIBUTE_KEY_MAPPER_RESET) && this.selectionRangeStart != null) {
			assert !this.selectionRangeStart.isAttached();
			this.selectionRangeStart = this.focusedRow;
		}

		this.tabIndex = uidl.hasAttribute("tabindex") ? uidl.getIntAttribute("tabindex") : 0;
		setProperTabIndex();

		resizeSortedColumnForSortIndicator();

		// Remember this to detect situations where overflow hack might be
		// needed during scrolling
		this.lastRenderedHeight = this.scrollBody.getOffsetHeight();

		this.rendering = false;
		this.headerChangedDuringUpdate = false;
	}

	private void initializeRows(UIDL uidl, UIDL rowData) {
		if (this.scrollBody != null) {
			this.scrollBody.removeFromParent();
			this.lazyUnregistryBag.add(this.scrollBody);
		}
		this.scrollBody = createScrollBody();

		this.scrollBody.renderInitialRows(rowData, uidl.getIntAttribute("firstrow"), uidl.getIntAttribute("rows"));
		this.scrollBodyPanel.add(this.scrollBody);

		// New body starts scrolled to the left, make sure the header and footer
		// are also scrolled to the left
		this.tHead.setHorizontalScrollPosition(0);
		this.tFoot.setHorizontalScrollPosition(0);

		this.initialContentReceived = true;
		if (isAttached()) {
			sizeInit();
		}
		this.scrollBody.restoreRowVisibility();
	}

	private void updateColumnProperties(UIDL uidl) {
		updateColumnOrder(uidl);

		updateCollapsedColumns(uidl);

		UIDL vc = uidl.getChildByTagName("visiblecolumns");
		if (vc != null) {
			this.tHead.updateCellsFromUIDL(vc);
			this.tFoot.updateCellsFromUIDL(vc);
		}

		updateHeader(uidl.getStringArrayAttribute("vcolorder"));
		updateFooter(uidl.getStringArrayAttribute("vcolorder"));
		if (uidl.hasVariable("noncollapsiblecolumns")) {
			this.noncollapsibleColumns = uidl.getStringArrayVariableAsSet("noncollapsiblecolumns");
		}
	}

	private void updateCollapsedColumns(UIDL uidl) {
		if (uidl.hasVariable("collapsedcolumns")) {
			this.tHead.setColumnCollapsingAllowed(true);
			this.collapsedColumns = uidl.getStringArrayVariableAsSet("collapsedcolumns");
		} else {
			this.tHead.setColumnCollapsingAllowed(false);
		}
	}

	private void updateColumnOrder(UIDL uidl) {
		if (uidl.hasVariable("columnorder")) {
			this.columnReordering = true;
			this.columnOrder = uidl.getStringArrayVariable("columnorder");
		} else {
			this.columnReordering = false;
			this.columnOrder = null;
		}
	}

	private boolean selectSelectedRows(UIDL uidl) {
		boolean keyboardSelectionOverRowFetchInProgress = false;

		if (uidl.hasVariable("selected")) {
			final Set<String> selectedKeys = uidl.getStringArrayVariableAsSet("selected");
			if (this.scrollBody != null) {
				Iterator<Widget> iterator = this.scrollBody.iterator();
				while (iterator.hasNext()) {
					/*
					 * Make the focus reflect to the server side state unless we
					 * are currently selecting multiple rows with keyboard.
					 */
					VScrollTableRow row = (VScrollTableRow) iterator.next();
					boolean selected = selectedKeys.contains(row.getKey());
					if (!selected && this.unSyncedselectionsBeforeRowFetch != null && this.unSyncedselectionsBeforeRowFetch.contains(row.getKey())) {
						selected = true;
						keyboardSelectionOverRowFetchInProgress = true;
					}
					if (selected != row.isSelected()) {
						row.toggleSelection();
						if (!isSingleSelectMode() && !selected) {
							// Update selection range in case a row is
							// unselected from the middle of a range - #8076
							removeRowFromUnsentSelectionRanges(row);
						}
					}
				}
			}
		}
		this.unSyncedselectionsBeforeRowFetch = null;
		return keyboardSelectionOverRowFetchInProgress;
	}

	private void updateSortingProperties(UIDL uidl) {
		this.oldSortColumn = this.sortColumn;
		if (uidl.hasVariable("sortascending")) {
			this.sortAscending = uidl.getBooleanVariable("sortascending");
			this.sortColumn = uidl.getStringVariable("sortcolumn");
		}
	}

	private void resizeSortedColumnForSortIndicator() {
		// Force recalculation of the captionContainer element inside the header
		// cell to accomodate for the size of the sort arrow.
		HeaderCell sortedHeader = this.tHead.getHeaderCell(this.sortColumn);
		if (sortedHeader != null) {
			this.tHead.resizeCaptionContainer(sortedHeader);
		}
		// Also recalculate the width of the captionContainer element in the
		// previously sorted header, since this now has more room.
		HeaderCell oldSortedHeader = this.tHead.getHeaderCell(this.oldSortColumn);
		if (oldSortedHeader != null) {
			this.tHead.resizeCaptionContainer(oldSortedHeader);
		}
	}

	private void updateFirstVisibleAndScrollIfNeeded(UIDL uidl) {
		this.firstvisible = uidl.hasVariable("firstvisible") ? uidl.getIntVariable("firstvisible") : 0;
		if (this.firstvisible != this.lastRequestedFirstvisible && this.scrollBody != null) {
			// received 'surprising' firstvisible from server: scroll there
			this.firstRowInViewPort = this.firstvisible;
			this.scrollBodyPanel.setScrollPosition(measureRowHeightOffset(this.firstvisible));
		}
	}

	protected int measureRowHeightOffset(int rowIx) {
		return (int) (rowIx * this.scrollBody.getRowHeight());
	}

	private void updatePageLength(UIDL uidl) {
		int oldPageLength = this.pageLength;
		if (uidl.hasAttribute("pagelength")) {
			this.pageLength = uidl.getIntAttribute("pagelength");
		} else {
			// pagelenght is "0" meaning scrolling is turned off
			this.pageLength = this.totalRows;
		}

		if (oldPageLength != this.pageLength && this.initializedAndAttached) {
			// page length changed, need to update size
			sizeInit();
		}
	}

	private void updateSelectionProperties(UIDL uidl) {
		setMultiSelectMode(uidl.hasAttribute("multiselectmode") ? uidl.getIntAttribute("multiselectmode") : MULTISELECT_MODE_DEFAULT);

		this.nullSelectionAllowed = uidl.hasAttribute("nsa") ? uidl.getBooleanAttribute("nsa") : true;

		if (uidl.hasAttribute("selectmode")) {
			if (uidl.getBooleanAttribute("readonly")) {
				this.selectMode = Table.SELECT_MODE_NONE;
			} else if (uidl.getStringAttribute("selectmode").equals("multi")) {
				this.selectMode = Table.SELECT_MODE_MULTI;
			} else if (uidl.getStringAttribute("selectmode").equals("single")) {
				this.selectMode = Table.SELECT_MODE_SINGLE;
			} else {
				this.selectMode = Table.SELECT_MODE_NONE;
			}
		}
	}

	private void updateDragMode(UIDL uidl) {
		this.dragmode = uidl.hasAttribute("dragmode") ? uidl.getIntAttribute("dragmode") : 0;
		if (BrowserInfo.get().isIE()) {
			if (this.dragmode > 0) {
				getElement().setPropertyJSO("onselectstart", getPreventTextSelectionIEHack());
			} else {
				getElement().setPropertyJSO("onselectstart", null);
			}
		}
	}

	protected void updateTotalRows(UIDL uidl) {
		int newTotalRows = uidl.getIntAttribute("totalrows");
		if (newTotalRows != getTotalRows()) {
			if (this.scrollBody != null) {
				if (getTotalRows() == 0) {
					this.tHead.clear();
					this.tFoot.clear();
				}
				this.initializedAndAttached = false;
				this.initialContentReceived = false;
				this.isNewBody = true;
			}
			setTotalRows(newTotalRows);
		}
	}

	protected void setTotalRows(int newTotalRows) {
		this.totalRows = newTotalRows;
	}

	protected int getTotalRows() {
		return this.totalRows;
	}

	private void focusRowFromBody() {
		if (this.selectedRowKeys.size() == 1) {
			// try to focus a row currently selected and in viewport
			String selectedRowKey = this.selectedRowKeys.iterator().next();
			if (selectedRowKey != null) {
				VScrollTableRow renderedRow = getRenderedRowByKey(selectedRowKey);
				if (renderedRow == null || !renderedRow.isInViewPort()) {
					setRowFocus(this.scrollBody.getRowByRowIndex(this.firstRowInViewPort));
				} else {
					setRowFocus(renderedRow);
				}
			}
		} else {
			// multiselect mode
			setRowFocus(this.scrollBody.getRowByRowIndex(this.firstRowInViewPort));
		}
	}

	protected VScrollTableBody createScrollBody() {
		return new VScrollTableBody();
	}

	/**
	 * Selects the last row visible in the table
	 * 
	 * @param focusOnly
	 *            Should the focus only be moved to the last row
	 */
	private void selectLastRenderedRowInViewPort(boolean focusOnly) {
		int index = this.firstRowInViewPort + getFullyVisibleRowCount();
		VScrollTableRow lastRowInViewport = this.scrollBody.getRowByRowIndex(index);
		if (lastRowInViewport == null) {
			// this should not happen in normal situations (white space at the
			// end of viewport). Select the last rendered as a fallback.
			lastRowInViewport = this.scrollBody.getRowByRowIndex(this.scrollBody.getLastRendered());
			if (lastRowInViewport == null) {
				return; // empty table
			}
		}
		setRowFocus(lastRowInViewport);
		if (!focusOnly) {
			selectFocusedRow(false, this.multiselectPending);
			sendSelectedRows();
		}
	}

	/**
	 * Selects the first row visible in the table
	 * 
	 * @param focusOnly
	 *            Should the focus only be moved to the first row
	 */
	private void selectFirstRenderedRowInViewPort(boolean focusOnly) {
		int index = this.firstRowInViewPort;
		VScrollTableRow firstInViewport = this.scrollBody.getRowByRowIndex(index);
		if (firstInViewport == null) {
			// this should not happen in normal situations
			return;
		}
		setRowFocus(firstInViewport);
		if (!focusOnly) {
			selectFocusedRow(false, this.multiselectPending);
			sendSelectedRows();
		}
	}

	private void setCacheRateFromUIDL(UIDL uidl) {
		setCacheRate(uidl.hasAttribute("cr") ? uidl.getDoubleAttribute("cr") : CACHE_RATE_DEFAULT);
	}

	private void setCacheRate(double d) {
		if (this.cache_rate != d) {
			this.cache_rate = d;
			this.cache_react_rate = 0.75 * d;
		}
	}

	/**
	 * Unregisters Paintables in "trashed" HasWidgets (IScrollTableBodys or
	 * IScrollTableRows). This is done lazily as Table must survive from
	 * "subtreecaching" logic.
	 */
	private void purgeUnregistryBag() {
		for (Iterator<Panel> iterator = this.lazyUnregistryBag.iterator(); iterator.hasNext();) {
			this.client.unregisterChildPaintables(iterator.next());
		}
		this.lazyUnregistryBag.clear();
	}

	private void updateActionMap(UIDL mainUidl) {
		UIDL actionsUidl = mainUidl.getChildByTagName("actions");
		if (actionsUidl == null) {
			return;
		}

		final Iterator<?> it = actionsUidl.getChildIterator();
		while (it.hasNext()) {
			final UIDL action = (UIDL) it.next();
			final String key = action.getStringAttribute("key");
			final String caption = action.getStringAttribute("caption");
			this.actionMap.put(key + "_c", caption);
			if (action.hasAttribute("icon")) {
				// TODO need some uri handling ??
				this.actionMap.put(key + "_i", this.client.translateVaadinUri(action.getStringAttribute("icon")));
			} else {
				this.actionMap.remove(key + "_i");
			}
		}

	}

	public String getActionCaption(String actionKey) {
		return this.actionMap.get(actionKey + "_c");
	}

	public String getActionIcon(String actionKey) {
		return this.actionMap.get(actionKey + "_i");
	}

	private void updateHeader(String[] strings) {
		if (strings == null) {
			return;
		}

		int visibleCols = strings.length;
		int colIndex = 0;
		if (this.showRowHeaders) {
			this.tHead.enableColumn(ROW_HEADER_COLUMN_KEY, colIndex);
			visibleCols++;
			this.visibleColOrder = new String[visibleCols];
			this.visibleColOrder[colIndex] = ROW_HEADER_COLUMN_KEY;
			colIndex++;
		} else {
			this.visibleColOrder = new String[visibleCols];
			this.tHead.removeCell(ROW_HEADER_COLUMN_KEY);
		}

		int i;
		for (i = 0; i < strings.length; i++) {
			final String cid = strings[i];
			this.visibleColOrder[colIndex] = cid;
			this.tHead.enableColumn(cid, colIndex);
			colIndex++;
		}

		this.tHead.setVisible(this.showColHeaders);
		setContainerHeight();

	}

	/**
	 * Updates footers.
	 * <p>
	 * Update headers whould be called before this method is called!
	 * </p>
	 * 
	 * @param strings
	 */
	private void updateFooter(String[] strings) {
		if (strings == null) {
			return;
		}

		// Add dummy column if row headers are present
		int colIndex = 0;
		if (this.showRowHeaders) {
			this.tFoot.enableColumn(ROW_HEADER_COLUMN_KEY, colIndex);
			colIndex++;
		} else {
			this.tFoot.removeCell(ROW_HEADER_COLUMN_KEY);
		}

		int i;
		for (i = 0; i < strings.length; i++) {
			final String cid = strings[i];
			this.tFoot.enableColumn(cid, colIndex);
			colIndex++;
		}

		this.tFoot.setVisible(this.showColFooters);
	}

	/**
	 * @param uidl
	 *            which contains row data
	 * @param firstRow
	 *            first row in data set
	 * @param reqRows
	 *            amount of rows in data set
	 */
	private void updateBody(UIDL uidl, int firstRow, int reqRows) {
		if (uidl == null || reqRows < 1) {
			// container is empty, remove possibly existing rows
			if (firstRow <= 0) {
				while (this.scrollBody.getLastRendered() > this.scrollBody.firstRendered) {
					this.scrollBody.unlinkRow(false);
				}
				this.scrollBody.unlinkRow(false);
			}
			return;
		}

		this.scrollBody.renderRows(uidl, firstRow, reqRows);

		discardRowsOutsideCacheWindow();
	}

	private void updateRowsInBody(UIDL partialRowUpdates) {
		if (partialRowUpdates == null) {
			return;
		}
		int firstRowIx = partialRowUpdates.getIntAttribute("firsturowix");
		int count = partialRowUpdates.getIntAttribute("numurows");
		this.scrollBody.unlinkRows(firstRowIx, count);
		this.scrollBody.insertRows(partialRowUpdates, firstRowIx, count);
	}

	/**
	 * Updates the internal cache by unlinking rows that fall outside of the
	 * caching window.
	 */
	protected void discardRowsOutsideCacheWindow() {
		int firstRowToKeep = (int) (this.firstRowInViewPort - this.pageLength * this.cache_rate);
		int lastRowToKeep = (int) (this.firstRowInViewPort + this.pageLength + this.pageLength * this.cache_rate);
		debug("Client side calculated cache rows to keep: " + firstRowToKeep + "-" + lastRowToKeep);

		if (this.serverCacheFirst != -1) {
			firstRowToKeep = this.serverCacheFirst;
			lastRowToKeep = this.serverCacheLast;
			debug("Server cache rows that override: " + this.serverCacheFirst + "-" + this.serverCacheLast);
			if (firstRowToKeep < this.scrollBody.getFirstRendered() || lastRowToKeep > this.scrollBody.getLastRendered()) {
				debug("*** Server wants us to keep " + this.serverCacheFirst + "-" + this.serverCacheLast + " but we only have rows "
				        + this.scrollBody.getFirstRendered() + "-" + this.scrollBody.getLastRendered() + " rendered!");
			}
		}
		discardRowsOutsideOf(firstRowToKeep, lastRowToKeep);

		this.scrollBody.fixSpacers();

		this.scrollBody.restoreRowVisibility();
	}

	private void discardRowsOutsideOf(int optimalFirstRow, int optimalLastRow) {
		/*
		 * firstDiscarded and lastDiscarded are only calculated for debug
		 * purposes
		 */
		int firstDiscarded = -1, lastDiscarded = -1;
		boolean cont = true;
		while (cont && this.scrollBody.getLastRendered() > optimalFirstRow && this.scrollBody.getFirstRendered() < optimalFirstRow) {
			if (firstDiscarded == -1) {
				firstDiscarded = this.scrollBody.getFirstRendered();
			}

			// removing row from start
			cont = this.scrollBody.unlinkRow(true);
		}
		if (firstDiscarded != -1) {
			lastDiscarded = this.scrollBody.getFirstRendered() - 1;
			debug("Discarded rows " + firstDiscarded + "-" + lastDiscarded);
		}
		firstDiscarded = lastDiscarded = -1;

		cont = true;
		while (cont && this.scrollBody.getLastRendered() > optimalLastRow) {
			if (lastDiscarded == -1) {
				lastDiscarded = this.scrollBody.getLastRendered();
			}

			// removing row from the end
			cont = this.scrollBody.unlinkRow(false);
		}
		if (lastDiscarded != -1) {
			firstDiscarded = this.scrollBody.getLastRendered() + 1;
			debug("Discarded rows " + firstDiscarded + "-" + lastDiscarded);
		}

		debug("Now in cache: " + this.scrollBody.getFirstRendered() + "-" + this.scrollBody.getLastRendered());
	}

	/**
	 * Inserts rows in the table body or removes them from the table body based
	 * on the commands in the UIDL.
	 * 
	 * @param partialRowAdditions
	 *            the UIDL containing row updates.
	 */
	protected void addAndRemoveRows(UIDL partialRowAdditions) {
		if (partialRowAdditions == null) {
			return;
		}
		if (partialRowAdditions.hasAttribute("hide")) {
			this.scrollBody.unlinkAndReindexRows(partialRowAdditions.getIntAttribute("firstprowix"), partialRowAdditions.getIntAttribute("numprows"));
			this.scrollBody.ensureCacheFilled();
		} else {
			if (partialRowAdditions.hasAttribute("delbelow")) {
				this.scrollBody.insertRowsDeleteBelow(partialRowAdditions, partialRowAdditions.getIntAttribute("firstprowix"),
				        partialRowAdditions.getIntAttribute("numprows"));
			} else {
				this.scrollBody.insertAndReindexRows(partialRowAdditions, partialRowAdditions.getIntAttribute("firstprowix"),
				        partialRowAdditions.getIntAttribute("numprows"));
			}
		}

		discardRowsOutsideCacheWindow();
	}

	/**
	 * Gives correct column index for given column key ("cid" in UIDL).
	 * 
	 * @param colKey
	 * @return column index of visible columns, -1 if column not visible
	 */
	private int getColIndexByKey(String colKey) {
		// return 0 if asked for rowHeaders
		if (ROW_HEADER_COLUMN_KEY.equals(colKey)) {
			return 0;
		}
		for (int i = 0; i < this.visibleColOrder.length; i++) {
			if (this.visibleColOrder[i].equals(colKey)) {
				return i;
			}
		}
		return -1;
	}

	private boolean isMultiSelectModeSimple() {
		return this.selectMode == Table.SELECT_MODE_MULTI && this.multiselectmode == MULTISELECT_MODE_SIMPLE;
	}

	private boolean isSingleSelectMode() {
		return this.selectMode == Table.SELECT_MODE_SINGLE;
	}

	private boolean isMultiSelectModeAny() {
		return this.selectMode == Table.SELECT_MODE_MULTI;
	}

	private boolean isMultiSelectModeDefault() {
		return this.selectMode == Table.SELECT_MODE_MULTI && this.multiselectmode == MULTISELECT_MODE_DEFAULT;
	}

	private void setMultiSelectMode(int multiselectmode) {
		if (BrowserInfo.get().isTouchDevice()) {
			// Always use the simple mode for touch devices that do not have
			// shift/ctrl keys
			this.multiselectmode = MULTISELECT_MODE_SIMPLE;
		} else {
			this.multiselectmode = multiselectmode;
		}

	}

	protected boolean isSelectable() {
		return this.selectMode > Table.SELECT_MODE_NONE;
	}

	private boolean isCollapsedColumn(String colKey) {
		if (this.collapsedColumns == null) {
			return false;
		}
		if (this.collapsedColumns.contains(colKey)) {
			return true;
		}
		return false;
	}

	private String getColKeyByIndex(int index) {
		return this.tHead.getHeaderCell(index).getColKey();
	}

	private void setColWidth(int colIndex, int w, boolean isDefinedWidth) {
		final HeaderCell hcell = this.tHead.getHeaderCell(colIndex);

		// Make sure that the column grows to accommodate the sort indicator if
		// necessary.
		if (w < hcell.getMinWidth()) {
			w = hcell.getMinWidth();
		}

		// Set header column width
		hcell.setWidth(w, isDefinedWidth);

		// Ensure indicators have been taken into account
		this.tHead.resizeCaptionContainer(hcell);

		// Set body column width
		this.scrollBody.setColWidth(colIndex, w);

		// Set footer column width
		FooterCell fcell = this.tFoot.getFooterCell(colIndex);
		fcell.setWidth(w, isDefinedWidth);
	}

	private int getColWidth(String colKey) {
		return this.tHead.getHeaderCell(colKey).getWidth();
	}

	/**
	 * Get a rendered row by its key
	 * 
	 * @param key
	 *            The key to search with
	 * @return
	 */
	protected VScrollTableRow getRenderedRowByKey(String key) {
		if (this.scrollBody != null) {
			final Iterator<Widget> it = this.scrollBody.iterator();
			VScrollTableRow r = null;
			while (it.hasNext()) {
				r = (VScrollTableRow) it.next();
				if (r.getKey().equals(key)) {
					return r;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the next row to the given row
	 * 
	 * @param row
	 *            The row to calculate from
	 * 
	 * @return The next row or null if no row exists
	 */
	private VScrollTableRow getNextRow(VScrollTableRow row, int offset) {
		final Iterator<Widget> it = this.scrollBody.iterator();
		VScrollTableRow r = null;
		while (it.hasNext()) {
			r = (VScrollTableRow) it.next();
			if (r == row) {
				r = null;
				while (offset >= 0 && it.hasNext()) {
					r = (VScrollTableRow) it.next();
					offset--;
				}
				return r;
			}
		}

		return null;
	}

	/**
	 * Returns the previous row from the given row
	 * 
	 * @param row
	 *            The row to calculate from
	 * @return The previous row or null if no row exists
	 */
	private VScrollTableRow getPreviousRow(VScrollTableRow row, int offset) {
		final Iterator<Widget> it = this.scrollBody.iterator();
		final Iterator<Widget> offsetIt = this.scrollBody.iterator();
		VScrollTableRow r = null;
		VScrollTableRow prev = null;
		while (it.hasNext()) {
			r = (VScrollTableRow) it.next();
			if (offset < 0) {
				prev = (VScrollTableRow) offsetIt.next();
			}
			if (r == row) {
				return prev;
			}
			offset--;
		}

		return null;
	}

	protected void reOrderColumn(String columnKey, int newIndex) {

		final int oldIndex = getColIndexByKey(columnKey);

		// Change header order
		this.tHead.moveCell(oldIndex, newIndex);

		// Change body order
		this.scrollBody.moveCol(oldIndex, newIndex);

		// Change footer order
		this.tFoot.moveCell(oldIndex, newIndex);

		/*
		 * Build new columnOrder and update it to server Note that columnOrder
		 * also contains collapsed columns so we cannot directly build it from
		 * cells vector Loop the old columnOrder and append in order to new
		 * array unless on moved columnKey. On new index also put the moved key
		 * i == index on columnOrder, j == index on newOrder
		 */
		final String oldKeyOnNewIndex = this.visibleColOrder[newIndex];
		if (this.showRowHeaders) {
			newIndex--; // columnOrder don't have rowHeader
		}
		// add back hidden rows,
		for (int i = 0; i < this.columnOrder.length; i++) {
			if (this.columnOrder[i].equals(oldKeyOnNewIndex)) {
				break; // break loop at target
			}
			if (isCollapsedColumn(this.columnOrder[i])) {
				newIndex++;
			}
		}
		// finally we can build the new columnOrder for server
		final String[] newOrder = new String[this.columnOrder.length];
		for (int i = 0, j = 0; j < newOrder.length; i++) {
			if (j == newIndex) {
				newOrder[j] = columnKey;
				j++;
			}
			if (i == this.columnOrder.length) {
				break;
			}
			if (this.columnOrder[i].equals(columnKey)) {
				continue;
			}
			newOrder[j] = this.columnOrder[i];
			j++;
		}
		this.columnOrder = newOrder;
		// also update visibleColumnOrder
		int i = this.showRowHeaders ? 1 : 0;
		for (int j = 0; j < newOrder.length; j++) {
			final String cid = newOrder[j];
			if (!isCollapsedColumn(cid)) {
				this.visibleColOrder[i++] = cid;
			}
		}
		this.client.updateVariable(this.paintableId, "columnorder", this.columnOrder, false);
		if (this.client.hasEventListeners(this, COLUMN_REORDER_EVENT_ID)) {
			this.client.sendPendingVariableChanges();
		}
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if (this.initialContentReceived) {
			sizeInit();
		}
	}

	@Override
	protected void onDetach() {
		this.rowRequestHandler.cancel();
		super.onDetach();
		// ensure that scrollPosElement will be detached
		if (this.scrollPositionElement != null) {
			final Element parent = DOM.getParent(this.scrollPositionElement);
			if (parent != null) {
				DOM.removeChild(parent, this.scrollPositionElement);
			}
		}
	}

	/**
	 * Run only once when component is attached and received its initial
	 * content. This function:
	 * 
	 * * Syncs headers and bodys "natural widths and saves the values.
	 * 
	 * * Sets proper width and height
	 * 
	 * * Makes deferred request to get some cache rows
	 */
	private void sizeInit() {
		/*
		 * We will use browsers table rendering algorithm to find proper column
		 * widths. If content and header take less space than available, we will
		 * divide extra space relatively to each column which has not width set.
		 * Overflow pixels are added to last column.
		 */

		Iterator<Widget> headCells = this.tHead.iterator();
		Iterator<Widget> footCells = this.tFoot.iterator();
		int i = 0;
		int totalExplicitColumnsWidths = 0;
		int total = 0;
		float expandRatioDivider = 0;

		final int[] widths = new int[this.tHead.visibleCells.size()];

		this.tHead.enableBrowserIntelligence();
		this.tFoot.enableBrowserIntelligence();

		// first loop: collect natural widths
		while (headCells.hasNext()) {
			final HeaderCell hCell = (HeaderCell) headCells.next();
			final FooterCell fCell = (FooterCell) footCells.next();
			int w = hCell.getWidth();
			if (hCell.isDefinedWidth()) {
				// server has defined column width explicitly
				totalExplicitColumnsWidths += w;
			} else {
				if (hCell.getExpandRatio() > 0) {
					expandRatioDivider += hCell.getExpandRatio();
					w = 0;
				} else {
					// get and store greater of header width and column width,
					// and
					// store it as a minimumn natural col width
					int headerWidth = hCell.getNaturalColumnWidth(i);
					int footerWidth = fCell.getNaturalColumnWidth(i);
					w = headerWidth > footerWidth ? headerWidth : footerWidth;
				}
				hCell.setNaturalMinimumColumnWidth(w);
				fCell.setNaturalMinimumColumnWidth(w);
			}
			widths[i] = w;
			total += w;
			i++;
		}

		this.tHead.disableBrowserIntelligence();
		this.tFoot.disableBrowserIntelligence();

		boolean willHaveScrollbarz = willHaveScrollbars();

		// fix "natural" width if width not set
		if (this.width == null || "".equals(this.width)) {
			int w = total;
			w += this.scrollBody.getCellExtraWidth() * this.visibleColOrder.length;
			if (willHaveScrollbarz) {
				w += Util.getNativeScrollbarSize();
			}
			setContentWidth(w);
		}

		int availW = this.scrollBody.getAvailableWidth();
		if (BrowserInfo.get().isIE()) {
			// Hey IE, are you really sure about this?
			availW = this.scrollBody.getAvailableWidth();
		}
		availW -= this.scrollBody.getCellExtraWidth() * this.visibleColOrder.length;

		if (willHaveScrollbarz) {
			availW -= Util.getNativeScrollbarSize();
		}

		// TODO refactor this code to be the same as in resize timer
		boolean needsReLayout = false;

		if (availW > total) {
			// natural size is smaller than available space
			final int extraSpace = availW - total;
			final int totalWidthR = total - totalExplicitColumnsWidths;
			int checksum = 0;
			needsReLayout = true;

			if (extraSpace == 1) {
				// We cannot divide one single pixel so we give it the first
				// undefined column
				headCells = this.tHead.iterator();
				i = 0;
				checksum = availW;
				while (headCells.hasNext()) {
					HeaderCell hc = (HeaderCell) headCells.next();
					if (!hc.isDefinedWidth()) {
						widths[i]++;
						break;
					}
					i++;
				}

			} else if (expandRatioDivider > 0) {
				// visible columns have some active expand ratios, excess
				// space is divided according to them
				headCells = this.tHead.iterator();
				i = 0;
				while (headCells.hasNext()) {
					HeaderCell hCell = (HeaderCell) headCells.next();
					if (hCell.getExpandRatio() > 0) {
						int w = widths[i];
						final int newSpace = Math.round((extraSpace * (hCell.getExpandRatio() / expandRatioDivider)));
						w += newSpace;
						widths[i] = w;
					}
					checksum += widths[i];
					i++;
				}
			} else if (totalWidthR > 0) {
				// no expand ratios defined, we will share extra space
				// relatively to "natural widths" among those without
				// explicit width
				headCells = this.tHead.iterator();
				i = 0;
				while (headCells.hasNext()) {
					HeaderCell hCell = (HeaderCell) headCells.next();
					if (!hCell.isDefinedWidth()) {
						int w = widths[i];
						final int newSpace = Math.round((float) extraSpace * (float) w / totalWidthR);
						w += newSpace;
						widths[i] = w;
					}
					checksum += widths[i];
					i++;
				}
			}

			if (extraSpace > 0 && checksum != availW) {
				/*
				 * There might be in some cases a rounding error of 1px when
				 * extra space is divided so if there is one then we give the
				 * first undefined column 1 more pixel
				 */
				headCells = this.tHead.iterator();
				i = 0;
				while (headCells.hasNext()) {
					HeaderCell hc = (HeaderCell) headCells.next();
					if (!hc.isDefinedWidth()) {
						widths[i] += availW - checksum;
						break;
					}
					i++;
				}
			}

		} else {
			// bodys size will be more than available and scrollbar will appear
		}

		// last loop: set possibly modified values or reset if new tBody
		i = 0;
		headCells = this.tHead.iterator();
		while (headCells.hasNext()) {
			final HeaderCell hCell = (HeaderCell) headCells.next();
			if (this.isNewBody || hCell.getWidth() == -1) {
				final int w = widths[i];
				setColWidth(i, w, false);
			}
			i++;
		}

		this.initializedAndAttached = true;

		if (needsReLayout) {
			this.scrollBody.reLayoutComponents();
		}

		updatePageLength();

		/*
		 * Fix "natural" height if height is not set. This must be after width
		 * fixing so the components' widths have been adjusted.
		 */
		if (this.height == null || "".equals(this.height)) {
			/*
			 * We must force an update of the row height as this point as it
			 * might have been (incorrectly) calculated earlier
			 */

			int bodyHeight;
			if (this.pageLength == this.totalRows) {
				/*
				 * A hack to support variable height rows when paging is off.
				 * Generally this is not supported by scrolltable. We want to
				 * show all rows so the bodyHeight should be equal to the table
				 * height.
				 */
				// int bodyHeight = scrollBody.getOffsetHeight();
				bodyHeight = this.scrollBody.getRequiredHeight();
			} else {
				bodyHeight = (int) Math.round(this.scrollBody.getRowHeight(true) * this.pageLength);
			}
			boolean needsSpaceForHorizontalSrollbar = (total > availW);
			if (needsSpaceForHorizontalSrollbar) {
				bodyHeight += Util.getNativeScrollbarSize();
			}
			this.scrollBodyPanel.setHeight(bodyHeight + "px");
			Util.runWebkitOverflowAutoFix(this.scrollBodyPanel.getElement());
		}

		this.isNewBody = false;

		if (this.firstvisible > 0) {
			// Deferred due to some Firefox oddities
			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					VOrchidScrollTable.this.scrollBodyPanel.setScrollPosition(measureRowHeightOffset(VOrchidScrollTable.this.firstvisible));
					VOrchidScrollTable.this.firstRowInViewPort = VOrchidScrollTable.this.firstvisible;
				}
			});
		}

		if (this.enabled) {
			// Do we need cache rows
			if (this.scrollBody.getLastRendered() + 1 < this.firstRowInViewPort + this.pageLength + (int) this.cache_react_rate * this.pageLength) {
				if (this.totalRows - 1 > this.scrollBody.getLastRendered()) {
					// fetch cache rows
					int firstInNewSet = this.scrollBody.getLastRendered() + 1;
					this.rowRequestHandler.setReqFirstRow(firstInNewSet);
					int lastInNewSet = (int) (this.firstRowInViewPort + this.pageLength + this.cache_rate * this.pageLength);
					if (lastInNewSet > this.totalRows - 1) {
						lastInNewSet = this.totalRows - 1;
					}
					this.rowRequestHandler.setReqRows(lastInNewSet - firstInNewSet + 1);
					this.rowRequestHandler.deferRowFetch(1);
				}
			}
		}

		/*
		 * Ensures the column alignments are correct at initial loading. <br/>
		 * (child components widths are correct)
		 */
		this.scrollBody.reLayoutComponents();
		Scheduler.get().scheduleDeferred(new Command() {
			@Override
			public void execute() {
				Util.runWebkitOverflowAutoFix(VOrchidScrollTable.this.scrollBodyPanel.getElement());
			}
		});
	}

	/**
	 * Note, this method is not official api although declared as protected.
	 * Extend at you own risk.
	 * 
	 * @return true if content area will have scrollbars visible.
	 */
	protected boolean willHaveScrollbars() {
		if (!(this.height != null && !this.height.equals(""))) {
			if (this.pageLength < this.totalRows) {
				return true;
			}
		} else {
			int fakeheight = (int) Math.round(this.scrollBody.getRowHeight() * this.totalRows);
			int availableHeight = this.scrollBodyPanel.getElement().getPropertyInt("clientHeight");
			if (fakeheight > availableHeight) {
				return true;
			}
		}
		return false;
	}

	private void announceScrollPosition() {
		if (this.scrollPositionElement == null) {
			this.scrollPositionElement = DOM.createDiv();
			this.scrollPositionElement.setClassName(CLASSNAME + "-scrollposition");
			this.scrollPositionElement.getStyle().setPosition(Position.ABSOLUTE);
			this.scrollPositionElement.getStyle().setDisplay(Display.NONE);
			getElement().appendChild(this.scrollPositionElement);
		}

		Style style = this.scrollPositionElement.getStyle();
		style.setMarginLeft(getElement().getOffsetWidth() / 2 - 80, Unit.PX);
		style.setMarginTop(-this.scrollBodyPanel.getOffsetHeight(), Unit.PX);

		// indexes go from 1-totalRows, as rowheaders in index-mode indicate
		int last = (this.firstRowInViewPort + this.pageLength);
		if (last > this.totalRows) {
			last = this.totalRows;
		}
		this.scrollPositionElement.setInnerHTML("<span>" + (this.firstRowInViewPort + 1) + " &ndash; " + (last) + "..." + "</span>");
		style.setDisplay(Display.BLOCK);
	}

	private void hideScrollPositionAnnotation() {
		if (this.scrollPositionElement != null) {
			DOM.setStyleAttribute(this.scrollPositionElement, "display", "none");
		}
	}

	private boolean isScrollPositionVisible() {
		return this.scrollPositionElement != null && !this.scrollPositionElement.getStyle().getDisplay().equals(Display.NONE.toString());
	}

	private class RowRequestHandler extends Timer {

		private int reqFirstRow = 0;
		private int reqRows = 0;
		private boolean isRunning = false;

		public void deferRowFetch() {
			deferRowFetch(250);
		}

		public boolean isRunning() {
			return this.isRunning;
		}

		public void deferRowFetch(int msec) {
			this.isRunning = true;
			if (this.reqRows > 0 && this.reqFirstRow < VOrchidScrollTable.this.totalRows) {
				schedule(msec);

				// tell scroll position to user if currently "visible" rows are
				// not rendered
				if (VOrchidScrollTable.this.totalRows > VOrchidScrollTable.this.pageLength
				        && ((VOrchidScrollTable.this.firstRowInViewPort + VOrchidScrollTable.this.pageLength > VOrchidScrollTable.this.scrollBody
				                .getLastRendered()) || (VOrchidScrollTable.this.firstRowInViewPort < VOrchidScrollTable.this.scrollBody.getFirstRendered()))) {
					announceScrollPosition();
				} else {
					hideScrollPositionAnnotation();
				}
			}
		}

		public void setReqFirstRow(int reqFirstRow) {
			if (reqFirstRow < 0) {
				reqFirstRow = 0;
			} else if (reqFirstRow >= VOrchidScrollTable.this.totalRows) {
				reqFirstRow = VOrchidScrollTable.this.totalRows - 1;
			}
			this.reqFirstRow = reqFirstRow;
		}

		public void setReqRows(int reqRows) {
			this.reqRows = reqRows;
		}

		@Override
		public void run() {
			if (VOrchidScrollTable.this.client.hasActiveRequest() || VOrchidScrollTable.this.navKeyDown) {
				// if client connection is busy, don't bother loading it more
				VConsole.log("Postponed rowfetch");
				schedule(250);
			} else {

				int firstToBeRendered = VOrchidScrollTable.this.scrollBody.firstRendered;
				if (this.reqFirstRow < firstToBeRendered) {
					firstToBeRendered = this.reqFirstRow;
				} else if (VOrchidScrollTable.this.firstRowInViewPort - (int) (VOrchidScrollTable.this.cache_rate * VOrchidScrollTable.this.pageLength) > firstToBeRendered) {
					firstToBeRendered = VOrchidScrollTable.this.firstRowInViewPort
					        - (int) (VOrchidScrollTable.this.cache_rate * VOrchidScrollTable.this.pageLength);
					if (firstToBeRendered < 0) {
						firstToBeRendered = 0;
					}
				}

				int lastToBeRendered = VOrchidScrollTable.this.scrollBody.lastRendered;

				if (this.reqFirstRow + this.reqRows - 1 > lastToBeRendered) {
					lastToBeRendered = this.reqFirstRow + this.reqRows - 1;
				} else if (VOrchidScrollTable.this.firstRowInViewPort + VOrchidScrollTable.this.pageLength + VOrchidScrollTable.this.pageLength
				        * VOrchidScrollTable.this.cache_rate < lastToBeRendered) {
					lastToBeRendered = (VOrchidScrollTable.this.firstRowInViewPort + VOrchidScrollTable.this.pageLength + (int) (VOrchidScrollTable.this.pageLength * VOrchidScrollTable.this.cache_rate));
					if (lastToBeRendered >= VOrchidScrollTable.this.totalRows) {
						lastToBeRendered = VOrchidScrollTable.this.totalRows - 1;
					}
					// due Safari 3.1 bug (see #2607), verify reqrows, original
					// problem unknown, but this should catch the issue
					if (this.reqFirstRow + this.reqRows - 1 > lastToBeRendered) {
						this.reqRows = lastToBeRendered - this.reqFirstRow;
					}
				}

				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "firstToBeRendered", firstToBeRendered, false);

				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "lastToBeRendered", lastToBeRendered, false);
				// remember which firstvisible we requested, in case the server
				// has
				// a differing opinion
				VOrchidScrollTable.this.lastRequestedFirstvisible = VOrchidScrollTable.this.firstRowInViewPort;
				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "firstvisible", VOrchidScrollTable.this.firstRowInViewPort,
				        false);
				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "reqfirstrow", this.reqFirstRow, false);
				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "reqrows", this.reqRows, true);

				if (VOrchidScrollTable.this.selectionChanged) {
					VOrchidScrollTable.this.unSyncedselectionsBeforeRowFetch = new HashSet<Object>(VOrchidScrollTable.this.selectedRowKeys);
				}
				this.isRunning = false;
			}
		}

		public int getReqFirstRow() {
			return this.reqFirstRow;
		}

		/**
		 * Sends request to refresh content at this position.
		 */
		public void refreshContent() {
			this.isRunning = true;
			int first = (int) (VOrchidScrollTable.this.firstRowInViewPort - VOrchidScrollTable.this.pageLength * VOrchidScrollTable.this.cache_rate);
			int reqRows = (int) (2 * VOrchidScrollTable.this.pageLength * VOrchidScrollTable.this.cache_rate + VOrchidScrollTable.this.pageLength);
			if (first < 0) {
				reqRows = reqRows + first;
				first = 0;
			}
			setReqFirstRow(first);
			setReqRows(reqRows);
			run();
		}
	}

	public class HeaderCell extends Widget {

		Element td = DOM.createTD();

		Element captionContainer = DOM.createDiv();

		Element sortIndicator = DOM.createDiv();

		Element colResizeWidget = DOM.createDiv();

		Element floatingCopyOfHeaderCell;

		private boolean sortable = false;
		private final String cid;
		private boolean dragging;

		private int dragStartX;
		private int colIndex;
		private int originalWidth;

		private boolean isResizing;

		private int headerX;

		private boolean moved;

		private int closestSlot;

		private int width = -1;

		private int naturalWidth = -1;

		private char align = ALIGN_LEFT;

		boolean definedWidth = false;

		private float expandRatio = 0;

		private boolean sorted;

		public void setSortable(boolean b) {
			this.sortable = b;
		}

		/**
		 * Makes room for the sorting indicator in case the column that the
		 * header cell belongs to is sorted. This is done by resizing the width
		 * of the caption container element by the correct amount
		 */
		public void resizeCaptionContainer(int rightSpacing) {

			int captionContainerWidth = this.width - this.colResizeWidget.getOffsetWidth() - rightSpacing;

			if (BrowserInfo.get().isIE6() || this.td.getClassName().contains("-asc") || this.td.getClassName().contains("-desc")) {
				// Leave room for the sort indicator
				captionContainerWidth -= this.sortIndicator.getOffsetWidth();
			}

			if (captionContainerWidth < 0) {
				rightSpacing += captionContainerWidth;
				captionContainerWidth = 0;
			}

			this.captionContainer.getStyle().setPropertyPx("width", captionContainerWidth);

			// Apply/Remove spacing if defined
			if (rightSpacing > 0) {
				this.colResizeWidget.getStyle().setMarginLeft(rightSpacing, Unit.PX);
			} else {
				this.colResizeWidget.getStyle().clearMarginLeft();
			}
		}

		public void setNaturalMinimumColumnWidth(int w) {
			this.naturalWidth = w;
		}

		public HeaderCell(String colId, String headerText) {
			this.cid = colId;

			DOM.setElementProperty(this.colResizeWidget, "className", CLASSNAME + "-resizer");

			setText(headerText);

			DOM.appendChild(this.td, this.colResizeWidget);

			DOM.setElementProperty(this.sortIndicator, "className", CLASSNAME + "-sort-indicator");
			DOM.appendChild(this.td, this.sortIndicator);

			DOM.setElementProperty(this.captionContainer, "className", CLASSNAME + "-caption-container");

			// ensure no clipping initially (problem on column additions)
			DOM.setStyleAttribute(this.captionContainer, "overflow", "visible");

			DOM.appendChild(this.td, this.captionContainer);

			DOM.sinkEvents(this.td, Event.MOUSEEVENTS | Event.ONDBLCLICK | Event.ONCONTEXTMENU | Event.TOUCHEVENTS);

			setElement(this.td);

			setAlign(ALIGN_LEFT);
		}

		public void disableAutoWidthCalculation() {
			this.definedWidth = true;
			this.expandRatio = 0;
		}

		public void setWidth(int w, boolean ensureDefinedWidth) {
			if (ensureDefinedWidth) {
				this.definedWidth = true;
				// on column resize expand ratio becomes zero
				this.expandRatio = 0;
			}
			if (this.width == -1) {
				// go to default mode, clip content if necessary
				DOM.setStyleAttribute(this.captionContainer, "overflow", "");
			}
			this.width = w;
			if (w == -1) {
				DOM.setStyleAttribute(this.captionContainer, "width", "");
				setWidth("");
			} else {
				VOrchidScrollTable.this.tHead.resizeCaptionContainer(this);

				/*
				 * if we already have tBody, set the header width properly, if
				 * not defer it. IE will fail with complex float in table header
				 * unless TD width is not explicitly set.
				 */
				if (VOrchidScrollTable.this.scrollBody != null) {
					int tdWidth = this.width + VOrchidScrollTable.this.scrollBody.getCellExtraWidth();
					setWidth(tdWidth + "px");
				} else {
					Scheduler.get().scheduleDeferred(new Command() {
						@Override
						public void execute() {
							int tdWidth = HeaderCell.this.width + VOrchidScrollTable.this.scrollBody.getCellExtraWidth();
							setWidth(tdWidth + "px");
						}
					});
				}
			}
		}

		public void setUndefinedWidth() {
			this.definedWidth = false;
			setWidth(-1, false);
		}

		/**
		 * Detects if width is fixed by developer on server side or resized to
		 * current width by user.
		 * 
		 * @return true if defined, false if "natural" width
		 */
		public boolean isDefinedWidth() {
			return this.definedWidth && this.width >= 0;
		}

		public int getWidth() {
			return this.width;
		}

		public void setText(String headerText) {
			DOM.setInnerHTML(this.captionContainer, headerText);
		}

		public String getColKey() {
			return this.cid;
		}

		private void setSorted(boolean sorted) {
			this.sorted = sorted;
			if (sorted) {
				if (VOrchidScrollTable.this.sortAscending) {
					this.setStyleName(CLASSNAME + "-header-cell-asc");
				} else {
					this.setStyleName(CLASSNAME + "-header-cell-desc");
				}
			} else {
				this.setStyleName(CLASSNAME + "-header-cell");
			}
		}

		/**
		 * Handle column reordering.
		 */
		@Override
		public void onBrowserEvent(Event event) {
			if (VOrchidScrollTable.this.enabled && event != null) {
				if (this.isResizing || event.getEventTarget().cast() == this.colResizeWidget) {
					if (this.dragging && (event.getTypeInt() == Event.ONMOUSEUP || event.getTypeInt() == Event.ONTOUCHEND)) {
						// Handle releasing column header on spacer #5318
						handleCaptionEvent(event);
					} else {
						onResizeEvent(event);
					}
				} else {
					/*
					 * Ensure focus before handling caption event. Otherwise
					 * variables changed from caption event may be before
					 * variables from other components that fire variables when
					 * they lose focus.
					 */
					if (event.getTypeInt() == Event.ONMOUSEDOWN || event.getTypeInt() == Event.ONTOUCHSTART) {
						VOrchidScrollTable.this.scrollBodyPanel.setFocus(true);
					}
					handleCaptionEvent(event);
					boolean stopPropagation = true;
					if (event.getTypeInt() == Event.ONCONTEXTMENU
					        && !VOrchidScrollTable.this.client.hasEventListeners(VOrchidScrollTable.this, HEADER_CLICK_EVENT_ID)) {
						// Prevent showing the browser's context menu only when
						// there is a header click listener.
						stopPropagation = false;
					}
					if (stopPropagation) {
						event.stopPropagation();
						event.preventDefault();
					}
				}
			}
		}

		private void createFloatingCopy() {
			this.floatingCopyOfHeaderCell = DOM.createDiv();
			DOM.setInnerHTML(this.floatingCopyOfHeaderCell, DOM.getInnerHTML(this.td));
			this.floatingCopyOfHeaderCell = DOM.getChild(this.floatingCopyOfHeaderCell, 2);
			DOM.setElementProperty(this.floatingCopyOfHeaderCell, "className", CLASSNAME + "-header-drag");
			// otherwise might wrap or be cut if narrow column
			DOM.setStyleAttribute(this.floatingCopyOfHeaderCell, "width", "auto");
			updateFloatingCopysPosition(DOM.getAbsoluteLeft(this.td), DOM.getAbsoluteTop(this.td));
			DOM.appendChild(RootPanel.get().getElement(), this.floatingCopyOfHeaderCell);
		}

		private void updateFloatingCopysPosition(int x, int y) {
			x -= DOM.getElementPropertyInt(this.floatingCopyOfHeaderCell, "offsetWidth") / 2;
			DOM.setStyleAttribute(this.floatingCopyOfHeaderCell, "left", x + "px");
			if (y > 0) {
				DOM.setStyleAttribute(this.floatingCopyOfHeaderCell, "top", (y + 7) + "px");
			}
		}

		private void hideFloatingCopy() {
			DOM.removeChild(RootPanel.get().getElement(), this.floatingCopyOfHeaderCell);
			this.floatingCopyOfHeaderCell = null;
		}

		/**
		 * Fires a header click event after the user has clicked a column header
		 * cell
		 * 
		 * @param event
		 *            The click event
		 */
		private void fireHeaderClickedEvent(Event event) {
			if (VOrchidScrollTable.this.client.hasEventListeners(VOrchidScrollTable.this, HEADER_CLICK_EVENT_ID)) {
				MouseEventDetails details = new MouseEventDetails(event);
				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "headerClickEvent", details.toString(), false);
				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "headerClickCID", this.cid, true);
			}
		}

		protected void handleCaptionEvent(Event event) {
			switch (DOM.eventGetType(event)) {
				case Event.ONTOUCHSTART:
				case Event.ONMOUSEDOWN:
					if (VOrchidScrollTable.this.columnReordering && Util.isTouchEventOrLeftMouseButton(event)) {
						if (event.getTypeInt() == Event.ONTOUCHSTART) {
							/*
							 * prevent using this event in e.g. scrolling
							 */
							event.stopPropagation();
						}
						this.dragging = true;
						this.moved = false;
						this.colIndex = getColIndexByKey(this.cid);
						DOM.setCapture(getElement());
						this.headerX = VOrchidScrollTable.this.tHead.getAbsoluteLeft();
						event.preventDefault(); // prevent selecting text &&
						                        // generated touch events
					}
					break;
				case Event.ONMOUSEUP:
				case Event.ONTOUCHEND:
				case Event.ONTOUCHCANCEL:
					if (VOrchidScrollTable.this.columnReordering && Util.isTouchEventOrLeftMouseButton(event)) {
						this.dragging = false;
						DOM.releaseCapture(getElement());
						if (this.moved) {
							hideFloatingCopy();
							VOrchidScrollTable.this.tHead.removeSlotFocus();
							if (this.closestSlot != this.colIndex && this.closestSlot != (this.colIndex + 1)) {
								if (this.closestSlot > this.colIndex) {
									reOrderColumn(this.cid, this.closestSlot - 1);
								} else {
									reOrderColumn(this.cid, this.closestSlot);
								}
							}
						}
						if (Util.isTouchEvent(event)) {
							/*
							 * Prevent using in e.g. scrolling and prevent
							 * generated events.
							 */
							event.preventDefault();
							event.stopPropagation();
						}
					}

					if (!this.moved) {
						// mouse event was a click to header -> sort column
						if (this.sortable && Util.isTouchEventOrLeftMouseButton(event)) {
							if (VOrchidScrollTable.this.sortColumn.equals(this.cid)) {
								// just toggle order
								VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "sortascending",
								        !VOrchidScrollTable.this.sortAscending, false);
							} else {
								// set table sorted by this column
								VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "sortcolumn", this.cid, false);
							}
							// get also cache columns at the same request
							VOrchidScrollTable.this.scrollBodyPanel.setScrollPosition(0);
							VOrchidScrollTable.this.firstvisible = 0;
							VOrchidScrollTable.this.rowRequestHandler.setReqFirstRow(0);
							VOrchidScrollTable.this.rowRequestHandler.setReqRows((int) (2 * VOrchidScrollTable.this.pageLength
							        * VOrchidScrollTable.this.cache_rate + VOrchidScrollTable.this.pageLength));
							VOrchidScrollTable.this.rowRequestHandler.deferRowFetch(); // some
							// validation +
							// defer 250ms
							VOrchidScrollTable.this.rowRequestHandler.cancel(); // instead
							                                                    // of
							                                                    // waiting
							VOrchidScrollTable.this.rowRequestHandler.run(); // run
							                                                 // immediately
						}
						fireHeaderClickedEvent(event);
						if (Util.isTouchEvent(event)) {
							/*
							 * Prevent using in e.g. scrolling and prevent
							 * generated events.
							 */
							event.preventDefault();
							event.stopPropagation();
						}
						break;
					}
					break;
				case Event.ONDBLCLICK:
					fireHeaderClickedEvent(event);
					break;
				case Event.ONTOUCHMOVE:
				case Event.ONMOUSEMOVE:
					if (this.dragging && Util.isTouchEventOrLeftMouseButton(event)) {
						if (event.getTypeInt() == Event.ONTOUCHMOVE) {
							/*
							 * prevent using this event in e.g. scrolling
							 */
							event.stopPropagation();
						}
						if (!this.moved) {
							createFloatingCopy();
							this.moved = true;
						}

						final int clientX = Util.getTouchOrMouseClientX(event);
						final int x = clientX + VOrchidScrollTable.this.tHead.hTableWrapper.getScrollLeft();
						int slotX = this.headerX;
						this.closestSlot = this.colIndex;
						int closestDistance = -1;
						int start = 0;
						if (VOrchidScrollTable.this.showRowHeaders) {
							start++;
						}
						final int visibleCellCount = VOrchidScrollTable.this.tHead.getVisibleCellCount();
						for (int i = start; i <= visibleCellCount; i++) {
							if (i > 0) {
								final String colKey = getColKeyByIndex(i - 1);
								slotX += getColWidth(colKey);
							}
							final int dist = Math.abs(x - slotX);
							if (closestDistance == -1 || dist < closestDistance) {
								closestDistance = dist;
								this.closestSlot = i;
							}
						}
						VOrchidScrollTable.this.tHead.focusSlot(this.closestSlot);

						updateFloatingCopysPosition(clientX, -1);
					}
					break;
				default:
					break;
			}
		}

		private void onResizeEvent(Event event) {
			switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEDOWN:
					if (!Util.isTouchEventOrLeftMouseButton(event)) {
						return;
					}
					this.isResizing = true;
					DOM.setCapture(getElement());
					this.dragStartX = DOM.eventGetClientX(event);
					this.colIndex = getColIndexByKey(this.cid);
					this.originalWidth = getWidth();
					DOM.eventPreventDefault(event);
					break;
				case Event.ONMOUSEUP:
					if (!Util.isTouchEventOrLeftMouseButton(event)) {
						return;
					}
					this.isResizing = false;
					DOM.releaseCapture(getElement());
					VOrchidScrollTable.this.tHead.disableAutoColumnWidthCalculation(this);

					// Ensure last header cell is taking into account possible
					// column selector
					HeaderCell lastCell = VOrchidScrollTable.this.tHead.getHeaderCell(VOrchidScrollTable.this.tHead.getVisibleCellCount() - 1);
					VOrchidScrollTable.this.tHead.resizeCaptionContainer(lastCell);
					triggerLazyColumnAdjustment(true);

					fireColumnResizeEvent(this.cid, this.originalWidth, getColWidth(this.cid));
					break;
				case Event.ONMOUSEMOVE:
					if (!Util.isTouchEventOrLeftMouseButton(event)) {
						return;
					}
					if (this.isResizing) {
						final int deltaX = DOM.eventGetClientX(event) - this.dragStartX;
						if (deltaX == 0) {
							return;
						}
						VOrchidScrollTable.this.tHead.disableAutoColumnWidthCalculation(this);

						int newWidth = this.originalWidth + deltaX;
						if (newWidth < getMinWidth()) {
							newWidth = getMinWidth();
						}
						setColWidth(this.colIndex, newWidth, true);
						triggerLazyColumnAdjustment(false);
						forceRealignColumnHeaders();
					}
					break;
				default:
					break;
			}
		}

		public int getMinWidth() {
			int cellExtraWidth = 0;
			if (VOrchidScrollTable.this.scrollBody != null) {
				cellExtraWidth += VOrchidScrollTable.this.scrollBody.getCellExtraWidth();
			}
			return cellExtraWidth + this.sortIndicator.getOffsetWidth();
		}

		public String getCaption() {
			return DOM.getInnerText(this.captionContainer);
		}

		public boolean isEnabled() {
			return getParent() != null;
		}

		public void setAlign(char c) {
			final String ALIGN_PREFIX = CLASSNAME + "-caption-container-align-";
			if (this.align != c) {
				this.captionContainer.removeClassName(ALIGN_PREFIX + "center");
				this.captionContainer.removeClassName(ALIGN_PREFIX + "right");
				this.captionContainer.removeClassName(ALIGN_PREFIX + "left");
				switch (c) {
					case ALIGN_CENTER:
						this.captionContainer.addClassName(ALIGN_PREFIX + "center");
						break;
					case ALIGN_RIGHT:
						this.captionContainer.addClassName(ALIGN_PREFIX + "right");
						break;
					default:
						this.captionContainer.addClassName(ALIGN_PREFIX + "left");
						break;
				}
			}
			this.align = c;
		}

		public char getAlign() {
			return this.align;
		}

		/**
		 * Detects the natural minimum width for the column of this header cell.
		 * If column is resized by user or the width is defined by server the
		 * actual width is returned. Else the natural min width is returned.
		 * 
		 * @param columnIndex
		 *            column index hint, if -1 (unknown) it will be detected
		 * 
		 * @return
		 */
		public int getNaturalColumnWidth(int columnIndex) {
			if (isDefinedWidth()) {
				return this.width;
			} else {
				if (this.naturalWidth < 0) {
					// This is recently revealed column. Try to detect a proper
					// value (greater of header and data
					// cols)

					int hw = this.captionContainer.getOffsetWidth() + VOrchidScrollTable.this.scrollBody.getCellExtraWidth();
					if (BrowserInfo.get().isGecko() || BrowserInfo.get().isIE7()) {
						hw += this.sortIndicator.getOffsetWidth();
					}
					if (columnIndex < 0) {
						columnIndex = 0;
						for (Iterator<Widget> it = VOrchidScrollTable.this.tHead.iterator(); it.hasNext(); columnIndex++) {
							if (it.next() == this) {
								break;
							}
						}
					}
					final int cw = VOrchidScrollTable.this.scrollBody.getColWidth(columnIndex);
					this.naturalWidth = (hw > cw ? hw : cw);
				}
				return this.naturalWidth;
			}
		}

		public void setExpandRatio(float floatAttribute) {
			if (floatAttribute != this.expandRatio) {
				triggerLazyColumnAdjustment(false);
			}
			this.expandRatio = floatAttribute;
		}

		public float getExpandRatio() {
			return this.expandRatio;
		}

		public boolean isSorted() {
			return this.sorted;
		}
	}

	/**
	 * HeaderCell that is header cell for row headers.
	 * 
	 * Reordering disabled and clicking on it resets sorting.
	 */
	public class RowHeadersHeaderCell extends HeaderCell {

		RowHeadersHeaderCell() {
			super(ROW_HEADER_COLUMN_KEY, "");
			this.setStyleName(CLASSNAME + "-header-cell-rowheader");
		}

		@Override
		protected void handleCaptionEvent(Event event) {
			// NOP: RowHeaders cannot be reordered
			// TODO It'd be nice to reset sorting here
		}
	}

	public class TableHead extends Panel implements ActionOwner {

		private static final int WRAPPER_WIDTH = 900000;

		ArrayList<Widget> visibleCells = new ArrayList<Widget>();

		HashMap<String, HeaderCell> availableCells = new HashMap<String, HeaderCell>();

		Element div = DOM.createDiv();
		Element hTableWrapper = DOM.createDiv();
		Element hTableContainer = DOM.createDiv();
		Element table = DOM.createTable();
		Element headerTableBody = DOM.createTBody();
		Element tr = DOM.createTR();

		private final Element columnSelector = DOM.createDiv();

		private int focusedSlot = -1;

		public TableHead() {
			if (BrowserInfo.get().isIE()) {
				this.table.setPropertyInt("cellSpacing", 0);
			}

			DOM.setStyleAttribute(this.hTableWrapper, "overflow", "hidden");
			DOM.setElementProperty(this.hTableWrapper, "className", CLASSNAME + "-header");

			// TODO move styles to CSS
			DOM.setElementProperty(this.columnSelector, "className", CLASSNAME + "-column-selector");
			DOM.setStyleAttribute(this.columnSelector, "display", "none");

			DOM.appendChild(this.table, this.headerTableBody);
			DOM.appendChild(this.headerTableBody, this.tr);
			DOM.appendChild(this.hTableContainer, this.table);
			DOM.appendChild(this.hTableWrapper, this.hTableContainer);
			DOM.appendChild(this.div, this.hTableWrapper);
			DOM.appendChild(this.div, this.columnSelector);
			setElement(this.div);

			setStyleName(CLASSNAME + "-header-wrap");

			DOM.sinkEvents(this.columnSelector, Event.ONCLICK);

			this.availableCells.put(ROW_HEADER_COLUMN_KEY, new RowHeadersHeaderCell());
		}

		public void resizeCaptionContainer(HeaderCell cell) {
			HeaderCell lastcell = getHeaderCell(this.visibleCells.size() - 1);

			// Measure column widths
			int columnTotalWidth = 0;
			for (Widget w : this.visibleCells) {
				columnTotalWidth += w.getOffsetWidth();
			}

			if (cell == lastcell && this.columnSelector.getOffsetWidth() > 0
			        && columnTotalWidth >= this.div.getOffsetWidth() - this.columnSelector.getOffsetWidth() && !hasVerticalScrollbar()) {
				// Ensure column caption is visible when placed under the column
				// selector widget by shifting and resizing the caption.
				int offset = 0;
				int diff = this.div.getOffsetWidth() - columnTotalWidth;
				if (diff < this.columnSelector.getOffsetWidth() && diff > 0) {
					// If the difference is less than the column selectors width
					// then just offset by the
					// difference
					offset = this.columnSelector.getOffsetWidth() - diff;
				} else {
					// Else offset by the whole column selector
					offset = this.columnSelector.getOffsetWidth();
				}
				lastcell.resizeCaptionContainer(offset);
			} else {
				cell.resizeCaptionContainer(0);
			}
		}

		@Override
		public void clear() {
			for (String cid : this.availableCells.keySet()) {
				removeCell(cid);
			}
			this.availableCells.clear();
			this.availableCells.put(ROW_HEADER_COLUMN_KEY, new RowHeadersHeaderCell());
		}

		public void updateCellsFromUIDL(UIDL uidl) {
			Iterator<?> it = uidl.getChildIterator();
			HashSet<String> updated = new HashSet<String>();
			boolean refreshContentWidths = false;
			while (it.hasNext()) {
				final UIDL col = (UIDL) it.next();
				final String cid = col.getStringAttribute("cid");
				updated.add(cid);

				String caption = buildCaptionHtmlSnippet(col);
				HeaderCell c = getHeaderCell(cid);
				if (c == null) {
					c = new HeaderCell(cid, caption);
					this.availableCells.put(cid, c);
					if (VOrchidScrollTable.this.initializedAndAttached) {
						// we will need a column width recalculation
						VOrchidScrollTable.this.initializedAndAttached = false;
						VOrchidScrollTable.this.initialContentReceived = false;
						VOrchidScrollTable.this.isNewBody = true;
					}
				} else {
					c.setText(caption);
				}

				if (col.hasAttribute("sortable")) {
					c.setSortable(true);
					if (cid.equals(VOrchidScrollTable.this.sortColumn)) {
						c.setSorted(true);
					} else {
						c.setSorted(false);
					}
				} else {
					c.setSortable(false);
				}

				if (col.hasAttribute("align")) {
					c.setAlign(col.getStringAttribute("align").charAt(0));
				} else {
					c.setAlign(ALIGN_LEFT);

				}
				if (col.hasAttribute("width")) {
					final String widthStr = col.getStringAttribute("width");
					// Make sure to accomodate for the sort indicator if
					// necessary.
					int width = Integer.parseInt(widthStr);
					if (width < c.getMinWidth()) {
						width = c.getMinWidth();
					}
					if (width != c.getWidth() && VOrchidScrollTable.this.scrollBody != null) {
						// Do a more thorough update if a column is resized from
						// the server *after* the header has been properly
						// initialized
						final int colIx = getColIndexByKey(c.cid);
						final int newWidth = width;
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {
							@Override
							public void execute() {
								setColWidth(colIx, newWidth, true);
							}
						});
						refreshContentWidths = true;
					} else {
						c.setWidth(width, true);
					}
				} else if (VOrchidScrollTable.this.recalcWidths) {
					c.setUndefinedWidth();
				}
				if (col.hasAttribute("er")) {
					c.setExpandRatio(col.getFloatAttribute("er"));
				}
				if (col.hasAttribute("collapsed")) {
					// ensure header is properly removed from parent (case when
					// collapsing happens via servers side api)
					if (c.isAttached()) {
						c.removeFromParent();
						VOrchidScrollTable.this.headerChangedDuringUpdate = true;
					}
				}
			}

			if (refreshContentWidths) {
				// Recalculate the column sizings if any column has changed
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						triggerLazyColumnAdjustment(true);
					}
				});
			}

			// check for orphaned header cells
			for (Iterator<String> cit = this.availableCells.keySet().iterator(); cit.hasNext();) {
				String cid = cit.next();
				if (!updated.contains(cid)) {
					removeCell(cid);
					cit.remove();
					// we will need a column width recalculation, since columns
					// with expand ratios should expand to fill the void.
					VOrchidScrollTable.this.initializedAndAttached = false;
					VOrchidScrollTable.this.initialContentReceived = false;
					VOrchidScrollTable.this.isNewBody = true;
				}
			}
		}

		public void enableColumn(String cid, int index) {
			final HeaderCell c = getHeaderCell(cid);
			if (!c.isEnabled() || getHeaderCell(index) != c) {
				setHeaderCell(index, c);
				if (VOrchidScrollTable.this.initializedAndAttached) {
					VOrchidScrollTable.this.headerChangedDuringUpdate = true;
				}
			}
		}

		public int getVisibleCellCount() {
			return this.visibleCells.size();
		}

		public void setHorizontalScrollPosition(int scrollLeft) {
			if (BrowserInfo.get().isIE6()) {
				this.hTableWrapper.getStyle().setPosition(Position.RELATIVE);
				this.hTableWrapper.getStyle().setLeft(-scrollLeft, Unit.PX);
			} else {
				this.hTableWrapper.setScrollLeft(scrollLeft);
			}
		}

		public void setColumnCollapsingAllowed(boolean cc) {
			if (cc) {
				this.columnSelector.getStyle().setDisplay(Display.BLOCK);
			} else {
				this.columnSelector.getStyle().setDisplay(Display.NONE);
			}
		}

		public void disableBrowserIntelligence() {
			this.hTableContainer.getStyle().setWidth(WRAPPER_WIDTH, Unit.PX);
		}

		public void enableBrowserIntelligence() {
			this.hTableContainer.getStyle().clearWidth();
		}

		public void setHeaderCell(int index, HeaderCell cell) {
			if (cell.isEnabled()) {
				// we're moving the cell
				DOM.removeChild(this.tr, cell.getElement());
				orphan(cell);
				this.visibleCells.remove(cell);
			}
			if (index < this.visibleCells.size()) {
				// insert to right slot
				DOM.insertChild(this.tr, cell.getElement(), index);
				adopt(cell);
				this.visibleCells.add(index, cell);
			} else if (index == this.visibleCells.size()) {
				// simply append
				DOM.appendChild(this.tr, cell.getElement());
				adopt(cell);
				this.visibleCells.add(cell);
			} else {
				throw new RuntimeException("Header cells must be appended in order");
			}
		}

		public HeaderCell getHeaderCell(int index) {
			if (index >= 0 && index < this.visibleCells.size()) {
				return (HeaderCell) this.visibleCells.get(index);
			} else {
				return null;
			}
		}

		/**
		 * Get's HeaderCell by it's column Key.
		 * 
		 * Note that this returns HeaderCell even if it is currently collapsed.
		 * 
		 * @param cid
		 *            Column key of accessed HeaderCell
		 * @return HeaderCell
		 */
		public HeaderCell getHeaderCell(String cid) {
			return this.availableCells.get(cid);
		}

		public void moveCell(int oldIndex, int newIndex) {
			final HeaderCell hCell = getHeaderCell(oldIndex);
			final Element cell = hCell.getElement();

			this.visibleCells.remove(oldIndex);
			DOM.removeChild(this.tr, cell);

			DOM.insertChild(this.tr, cell, newIndex);
			this.visibleCells.add(newIndex, hCell);
		}

		@Override
		public Iterator<Widget> iterator() {
			return this.visibleCells.iterator();
		}

		@Override
		public boolean remove(Widget w) {
			if (this.visibleCells.contains(w)) {
				this.visibleCells.remove(w);
				orphan(w);
				DOM.removeChild(DOM.getParent(w.getElement()), w.getElement());
				return true;
			}
			return false;
		}

		public void removeCell(String colKey) {
			final HeaderCell c = getHeaderCell(colKey);
			remove(c);
		}

		private void focusSlot(int index) {
			removeSlotFocus();
			if (index > 0) {
				DOM.setElementProperty(DOM.getFirstChild(DOM.getChild(this.tr, index - 1)), "className", CLASSNAME + "-resizer " + CLASSNAME
				        + "-focus-slot-right");
			} else {
				DOM.setElementProperty(DOM.getFirstChild(DOM.getChild(this.tr, index)), "className", CLASSNAME + "-resizer " + CLASSNAME + "-focus-slot-left");
			}
			this.focusedSlot = index;
		}

		private void removeSlotFocus() {
			if (this.focusedSlot < 0) {
				return;
			}
			if (this.focusedSlot == 0) {
				DOM.setElementProperty(DOM.getFirstChild(DOM.getChild(this.tr, this.focusedSlot)), "className", CLASSNAME + "-resizer");
			} else if (this.focusedSlot > 0) {
				DOM.setElementProperty(DOM.getFirstChild(DOM.getChild(this.tr, this.focusedSlot - 1)), "className", CLASSNAME + "-resizer");
			}
			this.focusedSlot = -1;
		}

		@Override
		public void onBrowserEvent(Event event) {
			if (VOrchidScrollTable.this.enabled) {
				if (event.getEventTarget().cast() == this.columnSelector) {
					final int left = DOM.getAbsoluteLeft(this.columnSelector);
					final int top = DOM.getAbsoluteTop(this.columnSelector) + DOM.getElementPropertyInt(this.columnSelector, "offsetHeight");
					VOrchidScrollTable.this.client.getContextMenu().showAt(this, left, top);
				}
			}
		}

		@Override
		protected void onDetach() {
			super.onDetach();
			if (VOrchidScrollTable.this.client != null) {
				VOrchidScrollTable.this.client.getContextMenu().ensureHidden(this);
			}
		}

		class VisibleColumnAction extends Action {

			String colKey;
			private boolean collapsed;
			private boolean noncollapsible = false;
			private final VScrollTableRow currentlyFocusedRow;

			public VisibleColumnAction(String colKey) {
				super(VOrchidScrollTable.TableHead.this);
				this.colKey = colKey;
				this.caption = VOrchidScrollTable.this.tHead.getHeaderCell(colKey).getCaption();
				this.currentlyFocusedRow = VOrchidScrollTable.this.focusedRow;
			}

			@Override
			public void execute() {
				if (this.noncollapsible) {
					return;
				}
				VOrchidScrollTable.this.client.getContextMenu().hide();
				// toggle selected column
				if (VOrchidScrollTable.this.collapsedColumns.contains(this.colKey)) {
					VOrchidScrollTable.this.collapsedColumns.remove(this.colKey);
				} else {
					VOrchidScrollTable.this.tHead.removeCell(this.colKey);
					VOrchidScrollTable.this.collapsedColumns.add(this.colKey);
					triggerLazyColumnAdjustment(true);
				}

				// update variable to server
				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "collapsedcolumns",
				        VOrchidScrollTable.this.collapsedColumns.toArray(new String[VOrchidScrollTable.this.collapsedColumns.size()]), false);
				// let rowRequestHandler determine proper rows
				VOrchidScrollTable.this.rowRequestHandler.refreshContent();
				lazyRevertFocusToRow(this.currentlyFocusedRow);
			}

			public void setCollapsed(boolean b) {
				this.collapsed = b;
			}

			public void setNoncollapsible(boolean b) {
				this.noncollapsible = b;
			}

			/**
			 * Override default method to distinguish on/off columns
			 */
			@Override
			public String getHTML() {
				final StringBuffer buf = new StringBuffer();
				buf.append("<span class=\"");
				if (this.collapsed) {
					buf.append("v-off");
				} else {
					buf.append("v-on");
				}
				if (this.noncollapsible) {
					buf.append(" v-disabled");
				}
				buf.append("\">");

				buf.append(super.getHTML());
				buf.append("</span>");

				return buf.toString();
			}

		}

		/*
		 * Returns columns as Action array for column select popup
		 */
		@Override
		public Action[] getActions() {
			Object[] cols;
			if (VOrchidScrollTable.this.columnReordering && VOrchidScrollTable.this.columnOrder != null) {
				cols = VOrchidScrollTable.this.columnOrder;
			} else {
				// if columnReordering is disabled, we need different way to get
				// all available columns
				cols = VOrchidScrollTable.this.visibleColOrder;
				cols = new Object[VOrchidScrollTable.this.visibleColOrder.length + VOrchidScrollTable.this.collapsedColumns.size()];
				int i;
				for (i = 0; i < VOrchidScrollTable.this.visibleColOrder.length; i++) {
					cols[i] = VOrchidScrollTable.this.visibleColOrder[i];
				}
				for (final Iterator<String> it = VOrchidScrollTable.this.collapsedColumns.iterator(); it.hasNext();) {
					cols[i++] = it.next();
				}
			}
			final Action[] actions = new Action[cols.length];

			for (int i = 0; i < cols.length; i++) {
				final String cid = (String) cols[i];
				final HeaderCell c = getHeaderCell(cid);
				final VisibleColumnAction a = new VisibleColumnAction(c.getColKey());
				a.setCaption(c.getCaption());
				if (!c.isEnabled()) {
					a.setCollapsed(true);
				}
				if (VOrchidScrollTable.this.noncollapsibleColumns.contains(cid)) {
					a.setNoncollapsible(true);
				}
				actions[i] = a;
			}
			return actions;
		}

		@Override
		public ApplicationConnection getClient() {
			return VOrchidScrollTable.this.client;
		}

		@Override
		public String getPaintableId() {
			return VOrchidScrollTable.this.paintableId;
		}

		/**
		 * Returns column alignments for visible columns
		 */
		public char[] getColumnAlignments() {
			final Iterator<Widget> it = this.visibleCells.iterator();
			final char[] aligns = new char[this.visibleCells.size()];
			int colIndex = 0;
			while (it.hasNext()) {
				aligns[colIndex++] = ((HeaderCell) it.next()).getAlign();
			}
			return aligns;
		}

		/**
		 * Disables the automatic calculation of all column widths by forcing
		 * the widths to be "defined" thus turning off expand ratios and such.
		 */
		public void disableAutoColumnWidthCalculation(HeaderCell source) {
			for (HeaderCell cell : this.availableCells.values()) {
				cell.disableAutoWidthCalculation();
			}
			// fire column resize events for all columns but the source of the
			// resize action, since an event will fire separately for this.
			ArrayList<HeaderCell> columns = new ArrayList<HeaderCell>(this.availableCells.values());
			columns.remove(source);
			sendColumnWidthUpdates(columns);
			forceRealignColumnHeaders();
		}
	}

	/**
	 * A cell in the footer
	 */
	public class FooterCell extends Widget {
		private final Element td = DOM.createTD();
		private final Element captionContainer = DOM.createDiv();
		private char align = ALIGN_LEFT;
		private int width = -1;
		private float expandRatio = 0;
		private final String cid;
		boolean definedWidth = false;
		private int naturalWidth = -1;

		public FooterCell(String colId, String headerText) {
			this.cid = colId;

			setText(headerText);

			DOM.setElementProperty(this.captionContainer, "className", CLASSNAME + "-footer-container");

			// ensure no clipping initially (problem on column additions)
			DOM.setStyleAttribute(this.captionContainer, "overflow", "visible");

			DOM.sinkEvents(this.captionContainer, Event.MOUSEEVENTS);

			DOM.appendChild(this.td, this.captionContainer);

			DOM.sinkEvents(this.td, Event.MOUSEEVENTS | Event.ONDBLCLICK | Event.ONCONTEXTMENU);

			setElement(this.td);
		}

		/**
		 * Sets the text of the footer
		 * 
		 * @param footerText
		 *            The text in the footer
		 */
		public void setText(String footerText) {
			if (footerText == null || footerText.equals("")) {
				footerText = "&nbsp;";
			}

			DOM.setInnerHTML(this.captionContainer, footerText);
		}

		/**
		 * Set alignment of the text in the cell
		 * 
		 * @param c
		 *            The alignment which can be ALIGN_CENTER, ALIGN_LEFT,
		 *            ALIGN_RIGHT
		 */
		public void setAlign(char c) {
			if (this.align != c) {
				switch (c) {
					case ALIGN_CENTER:
						DOM.setStyleAttribute(this.captionContainer, "textAlign", "center");
						break;
					case ALIGN_RIGHT:
						DOM.setStyleAttribute(this.captionContainer, "textAlign", "right");
						break;
					default:
						DOM.setStyleAttribute(this.captionContainer, "textAlign", "");
						break;
				}
			}
			this.align = c;
		}

		/**
		 * Get the alignment of the text int the cell
		 * 
		 * @return Returns either ALIGN_CENTER, ALIGN_LEFT or ALIGN_RIGHT
		 */
		public char getAlign() {
			return this.align;
		}

		/**
		 * Sets the width of the cell
		 * 
		 * @param w
		 *            The width of the cell
		 * @param ensureDefinedWidth
		 *            Ensures the the given width is not recalculated
		 */
		public void setWidth(int w, boolean ensureDefinedWidth) {

			if (ensureDefinedWidth) {
				this.definedWidth = true;
				// on column resize expand ratio becomes zero
				this.expandRatio = 0;
			}
			if (this.width == w) {
				return;
			}
			if (this.width == -1) {
				// go to default mode, clip content if necessary
				DOM.setStyleAttribute(this.captionContainer, "overflow", "");
			}
			this.width = w;
			if (w == -1) {
				DOM.setStyleAttribute(this.captionContainer, "width", "");
				setWidth("");
			} else {
				/*
				 * Reduce width with one pixel for the right border since the
				 * footers does not have any spacers between them.
				 */
				final int borderWidths = 1;

				// Set the container width (check for negative value)
				this.captionContainer.getStyle().setPropertyPx("width", Math.max(w - borderWidths, 0));

				/*
				 * if we already have tBody, set the header width properly, if
				 * not defer it. IE will fail with complex float in table header
				 * unless TD width is not explicitly set.
				 */
				if (VOrchidScrollTable.this.scrollBody != null) {
					int tdWidth = this.width + VOrchidScrollTable.this.scrollBody.getCellExtraWidth() - borderWidths;
					setWidth(Math.max(tdWidth, 0) + "px");
				} else {
					Scheduler.get().scheduleDeferred(new Command() {
						@Override
						public void execute() {
							int tdWidth = FooterCell.this.width + VOrchidScrollTable.this.scrollBody.getCellExtraWidth() - borderWidths;
							setWidth(Math.max(tdWidth, 0) + "px");
						}
					});
				}
			}
		}

		/**
		 * Sets the width to undefined
		 */
		public void setUndefinedWidth() {
			setWidth(-1, false);
		}

		/**
		 * Detects if width is fixed by developer on server side or resized to
		 * current width by user.
		 * 
		 * @return true if defined, false if "natural" width
		 */
		public boolean isDefinedWidth() {
			return this.definedWidth && this.width >= 0;
		}

		/**
		 * Returns the pixels width of the footer cell
		 * 
		 * @return The width in pixels
		 */
		public int getWidth() {
			return this.width;
		}

		/**
		 * Sets the expand ratio of the cell
		 * 
		 * @param floatAttribute
		 *            The expand ratio
		 */
		public void setExpandRatio(float floatAttribute) {
			this.expandRatio = floatAttribute;
		}

		/**
		 * Returns the expand ration of the cell
		 * 
		 * @return The expand ratio
		 */
		public float getExpandRatio() {
			return this.expandRatio;
		}

		/**
		 * Is the cell enabled?
		 * 
		 * @return True if enabled else False
		 */
		public boolean isEnabled() {
			return getParent() != null;
		}

		/**
		 * Handle column clicking
		 */

		@Override
		public void onBrowserEvent(Event event) {
			if (VOrchidScrollTable.this.enabled && event != null) {
				handleCaptionEvent(event);

				if (DOM.eventGetType(event) == Event.ONMOUSEUP) {
					VOrchidScrollTable.this.scrollBodyPanel.setFocus(true);
				}
				boolean stopPropagation = true;
				if (event.getTypeInt() == Event.ONCONTEXTMENU
				        && !VOrchidScrollTable.this.client.hasEventListeners(VOrchidScrollTable.this, FOOTER_CLICK_EVENT_ID)) {
					// Show browser context menu if a footer click listener is
					// not present
					stopPropagation = false;
				}
				if (stopPropagation) {
					event.stopPropagation();
					event.preventDefault();
				}
			}
		}

		/**
		 * Handles a event on the captions
		 * 
		 * @param event
		 *            The event to handle
		 */
		protected void handleCaptionEvent(Event event) {
			if (event.getTypeInt() == Event.ONMOUSEUP || event.getTypeInt() == Event.ONDBLCLICK) {
				fireFooterClickedEvent(event);
			}
		}

		/**
		 * Fires a footer click event after the user has clicked a column footer
		 * cell
		 * 
		 * @param event
		 *            The click event
		 */
		private void fireFooterClickedEvent(Event event) {
			if (VOrchidScrollTable.this.client.hasEventListeners(VOrchidScrollTable.this, FOOTER_CLICK_EVENT_ID)) {
				MouseEventDetails details = new MouseEventDetails(event);
				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "footerClickEvent", details.toString(), false);
				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "footerClickCID", this.cid, true);
			}
		}

		/**
		 * Returns the column key of the column
		 * 
		 * @return The column key
		 */
		public String getColKey() {
			return this.cid;
		}

		/**
		 * Detects the natural minimum width for the column of this header cell.
		 * If column is resized by user or the width is defined by server the
		 * actual width is returned. Else the natural min width is returned.
		 * 
		 * @param columnIndex
		 *            column index hint, if -1 (unknown) it will be detected
		 * 
		 * @return
		 */
		public int getNaturalColumnWidth(int columnIndex) {
			if (isDefinedWidth()) {
				return this.width;
			} else {
				if (this.naturalWidth < 0) {
					// This is recently revealed column. Try to detect a proper
					// value (greater of header and data
					// cols)

					final int hw = ((Element) getElement().getLastChild()).getOffsetWidth() + VOrchidScrollTable.this.scrollBody.getCellExtraWidth();
					if (columnIndex < 0) {
						columnIndex = 0;
						for (Iterator<Widget> it = VOrchidScrollTable.this.tHead.iterator(); it.hasNext(); columnIndex++) {
							if (it.next() == this) {
								break;
							}
						}
					}
					final int cw = VOrchidScrollTable.this.scrollBody.getColWidth(columnIndex);
					this.naturalWidth = (hw > cw ? hw : cw);
				}
				return this.naturalWidth;
			}
		}

		public void setNaturalMinimumColumnWidth(int w) {
			this.naturalWidth = w;
		}
	}

	/**
	 * HeaderCell that is header cell for row headers.
	 * 
	 * Reordering disabled and clicking on it resets sorting.
	 */
	public class RowHeadersFooterCell extends FooterCell {

		RowHeadersFooterCell() {
			super(ROW_HEADER_COLUMN_KEY, "");
		}

		@Override
		protected void handleCaptionEvent(Event event) {
			// NOP: RowHeaders cannot be reordered
			// TODO It'd be nice to reset sorting here
		}
	}

	/**
	 * The footer of the table which can be seen in the bottom of the Table.
	 */
	public class TableFooter extends Panel {

		private static final int WRAPPER_WIDTH = 900000;

		ArrayList<Widget> visibleCells = new ArrayList<Widget>();
		HashMap<String, FooterCell> availableCells = new HashMap<String, FooterCell>();

		Element div = DOM.createDiv();
		Element hTableWrapper = DOM.createDiv();
		Element hTableContainer = DOM.createDiv();
		Element table = DOM.createTable();
		Element headerTableBody = DOM.createTBody();
		Element tr = DOM.createTR();

		public TableFooter() {

			DOM.setStyleAttribute(this.hTableWrapper, "overflow", "hidden");
			DOM.setElementProperty(this.hTableWrapper, "className", CLASSNAME + "-footer");

			DOM.appendChild(this.table, this.headerTableBody);
			DOM.appendChild(this.headerTableBody, this.tr);
			DOM.appendChild(this.hTableContainer, this.table);
			DOM.appendChild(this.hTableWrapper, this.hTableContainer);
			DOM.appendChild(this.div, this.hTableWrapper);
			setElement(this.div);

			setStyleName(CLASSNAME + "-footer-wrap");

			this.availableCells.put(ROW_HEADER_COLUMN_KEY, new RowHeadersFooterCell());
		}

		@Override
		public void clear() {
			for (String cid : this.availableCells.keySet()) {
				removeCell(cid);
			}
			this.availableCells.clear();
			this.availableCells.put(ROW_HEADER_COLUMN_KEY, new RowHeadersFooterCell());
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.google.gwt.user.client.ui.Panel#remove(com.google.gwt.user.client
		 * .ui.Widget)
		 */
		@Override
		public boolean remove(Widget w) {
			if (this.visibleCells.contains(w)) {
				this.visibleCells.remove(w);
				orphan(w);
				DOM.removeChild(DOM.getParent(w.getElement()), w.getElement());
				return true;
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
		 */
		@Override
		public Iterator<Widget> iterator() {
			return this.visibleCells.iterator();
		}

		/**
		 * Gets a footer cell which represents the given columnId
		 * 
		 * @param cid
		 *            The columnId
		 * 
		 * @return The cell
		 */
		public FooterCell getFooterCell(String cid) {
			return this.availableCells.get(cid);
		}

		/**
		 * Gets a footer cell by using a column index
		 * 
		 * @param index
		 *            The index of the column
		 * @return The Cell
		 */
		public FooterCell getFooterCell(int index) {
			if (index < this.visibleCells.size()) {
				return (FooterCell) this.visibleCells.get(index);
			} else {
				return null;
			}
		}

		/**
		 * Updates the cells contents when updateUIDL request is received
		 * 
		 * @param uidl
		 *            The UIDL
		 */
		public void updateCellsFromUIDL(UIDL uidl) {
			Iterator<?> columnIterator = uidl.getChildIterator();
			HashSet<String> updated = new HashSet<String>();
			while (columnIterator.hasNext()) {
				final UIDL col = (UIDL) columnIterator.next();
				final String cid = col.getStringAttribute("cid");
				updated.add(cid);

				String caption = col.hasAttribute("fcaption") ? col.getStringAttribute("fcaption") : "";
				FooterCell c = getFooterCell(cid);
				if (c == null) {
					c = new FooterCell(cid, caption);
					this.availableCells.put(cid, c);
					if (VOrchidScrollTable.this.initializedAndAttached) {
						// we will need a column width recalculation
						VOrchidScrollTable.this.initializedAndAttached = false;
						VOrchidScrollTable.this.initialContentReceived = false;
						VOrchidScrollTable.this.isNewBody = true;
					}
				} else {
					c.setText(caption);
				}

				if (col.hasAttribute("align")) {
					c.setAlign(col.getStringAttribute("align").charAt(0));
				} else {
					c.setAlign(ALIGN_LEFT);

				}
				if (col.hasAttribute("width")) {
					if (VOrchidScrollTable.this.scrollBody == null) {
						// Already updated by setColWidth called from
						// TableHeads.updateCellsFromUIDL in case of a server
						// side resize
						final String width = col.getStringAttribute("width");
						c.setWidth(Integer.parseInt(width), true);
					}
				} else if (VOrchidScrollTable.this.recalcWidths) {
					c.setUndefinedWidth();
				}
				if (col.hasAttribute("er")) {
					c.setExpandRatio(col.getFloatAttribute("er"));
				}
				if (col.hasAttribute("collapsed")) {
					// ensure header is properly removed from parent (case when
					// collapsing happens via servers side api)
					if (c.isAttached()) {
						c.removeFromParent();
						VOrchidScrollTable.this.headerChangedDuringUpdate = true;
					}
				}
			}

			// check for orphaned header cells
			for (Iterator<String> cit = this.availableCells.keySet().iterator(); cit.hasNext();) {
				String cid = cit.next();
				if (!updated.contains(cid)) {
					removeCell(cid);
					cit.remove();
				}
			}
		}

		/**
		 * Set a footer cell for a specified column index
		 * 
		 * @param index
		 *            The index
		 * @param cell
		 *            The footer cell
		 */
		public void setFooterCell(int index, FooterCell cell) {
			if (cell.isEnabled()) {
				// we're moving the cell
				DOM.removeChild(this.tr, cell.getElement());
				orphan(cell);
				this.visibleCells.remove(cell);
			}
			if (index < this.visibleCells.size()) {
				// insert to right slot
				DOM.insertChild(this.tr, cell.getElement(), index);
				adopt(cell);
				this.visibleCells.add(index, cell);
			} else if (index == this.visibleCells.size()) {
				// simply append
				DOM.appendChild(this.tr, cell.getElement());
				adopt(cell);
				this.visibleCells.add(cell);
			} else {
				throw new RuntimeException("Header cells must be appended in order");
			}
		}

		/**
		 * Remove a cell by using the columnId
		 * 
		 * @param colKey
		 *            The columnId to remove
		 */
		public void removeCell(String colKey) {
			final FooterCell c = getFooterCell(colKey);
			remove(c);
		}

		/**
		 * Enable a column (Sets the footer cell)
		 * 
		 * @param cid
		 *            The columnId
		 * @param index
		 *            The index of the column
		 */
		public void enableColumn(String cid, int index) {
			final FooterCell c = getFooterCell(cid);
			if (!c.isEnabled() || getFooterCell(index) != c) {
				setFooterCell(index, c);
				if (VOrchidScrollTable.this.initializedAndAttached) {
					VOrchidScrollTable.this.headerChangedDuringUpdate = true;
				}
			}
		}

		/**
		 * Disable browser measurement of the table width
		 */
		public void disableBrowserIntelligence() {
			DOM.setStyleAttribute(this.hTableContainer, "width", WRAPPER_WIDTH + "px");
		}

		/**
		 * Enable browser measurement of the table width
		 */
		public void enableBrowserIntelligence() {
			DOM.setStyleAttribute(this.hTableContainer, "width", "");
		}

		/**
		 * Set the horizontal position in the cell in the footer. This is done
		 * when a horizontal scrollbar is present.
		 * 
		 * @param scrollLeft
		 *            The value of the leftScroll
		 */
		public void setHorizontalScrollPosition(int scrollLeft) {
			if (BrowserInfo.get().isIE6()) {
				this.hTableWrapper.getStyle().setProperty("position", "relative");
				this.hTableWrapper.getStyle().setPropertyPx("left", -scrollLeft);
			} else {
				this.hTableWrapper.setScrollLeft(scrollLeft);
			}
		}

		/**
		 * Swap cells when the column are dragged
		 * 
		 * @param oldIndex
		 *            The old index of the cell
		 * @param newIndex
		 *            The new index of the cell
		 */
		public void moveCell(int oldIndex, int newIndex) {
			final FooterCell hCell = getFooterCell(oldIndex);
			final Element cell = hCell.getElement();

			this.visibleCells.remove(oldIndex);
			DOM.removeChild(this.tr, cell);

			DOM.insertChild(this.tr, cell, newIndex);
			this.visibleCells.add(newIndex, hCell);
		}
	}

	/**
	 * This Panel can only contain VScrollTableRow type of widgets. This
	 * "simulates" very large table, keeping spacers which take room of
	 * unrendered rows.
	 * 
	 */
	public class VScrollTableBody extends com.google.gwt.user.client.ui.Panel {

		public static final int DEFAULT_ROW_HEIGHT = 24;

		private double rowHeight = -1;

		private final LinkedList<Widget> renderedRows = new LinkedList<Widget>();

		/**
		 * Due some optimizations row height measuring is deferred and initial
		 * set of rows is rendered detached. Flag set on when table body has
		 * been attached in dom and rowheight has been measured.
		 */
		private boolean tBodyMeasurementsDone = false;

		Element preSpacer = DOM.createDiv();
		Element postSpacer = DOM.createDiv();

		Element container = DOM.createDiv();

		TableSectionElement tBodyElement = Document.get().createTBodyElement();
		Element table = DOM.createTable();

		private int firstRendered;
		private int lastRendered;

		private char[] aligns;

		protected VScrollTableBody() {
			constructDOM();
			setElement(this.container);
		}

		public VScrollTableRow getRowByRowIndex(int indexInTable) {
			int internalIndex = indexInTable - this.firstRendered;
			if (internalIndex >= 0 && internalIndex < this.renderedRows.size()) {
				return (VScrollTableRow) this.renderedRows.get(internalIndex);
			} else {
				return null;
			}
		}

		/**
		 * @return the height of scrollable body, subpixels ceiled.
		 */
		public int getRequiredHeight() {
			return this.preSpacer.getOffsetHeight() + this.postSpacer.getOffsetHeight() + Util.getRequiredHeight(this.table);
		}

		private void constructDOM() {
			DOM.setElementProperty(this.table, "className", CLASSNAME + "-table");
			if (BrowserInfo.get().isIE()) {
				this.table.setPropertyInt("cellSpacing", 0);
			}
			DOM.setElementProperty(this.preSpacer, "className", CLASSNAME + "-row-spacer");
			DOM.setElementProperty(this.postSpacer, "className", CLASSNAME + "-row-spacer");

			this.table.appendChild(this.tBodyElement);
			DOM.appendChild(this.container, this.preSpacer);
			DOM.appendChild(this.container, this.table);
			DOM.appendChild(this.container, this.postSpacer);
			if (BrowserInfo.get().requiresTouchScrollDelegate()) {
				NodeList<Node> childNodes = this.container.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); i++) {
					Element item = (Element) childNodes.getItem(i);
					item.getStyle().setProperty("webkitTransform", "translate3d(0,0,0)");
				}
			}

		}

		public int getAvailableWidth() {
			int availW = VOrchidScrollTable.this.scrollBodyPanel.getOffsetWidth() - getBorderWidth();
			return availW;
		}

		public void renderInitialRows(UIDL rowData, int firstIndex, int rows) {
			this.firstRendered = firstIndex;
			this.lastRendered = firstIndex + rows - 1;
			final Iterator<?> it = rowData.getChildIterator();
			this.aligns = VOrchidScrollTable.this.tHead.getColumnAlignments();
			while (it.hasNext()) {
				final VScrollTableRow row = createRow((UIDL) it.next(), this.aligns);
				addRow(row);
			}
			if (isAttached()) {
				fixSpacers();
			}
		}

		public void renderRows(UIDL rowData, int firstIndex, int rows) {
			// FIXME REVIEW
			this.aligns = VOrchidScrollTable.this.tHead.getColumnAlignments();
			final Iterator<?> it = rowData.getChildIterator();
			if (firstIndex == this.lastRendered + 1) {
				while (it.hasNext()) {
					final VScrollTableRow row = prepareRow((UIDL) it.next());
					addRow(row);
					this.lastRendered++;
				}
				fixSpacers();
			} else if (firstIndex + rows == this.firstRendered) {
				final VScrollTableRow[] rowArray = new VScrollTableRow[rows];
				int i = rows;
				while (it.hasNext()) {
					i--;
					rowArray[i] = prepareRow((UIDL) it.next());
				}
				for (i = 0; i < rows; i++) {
					addRowBeforeFirstRendered(rowArray[i]);
					this.firstRendered--;
				}
			} else {
				// completely new set of rows
				while (this.lastRendered + 1 > this.firstRendered) {
					unlinkRow(false);
				}
				final VScrollTableRow row = prepareRow((UIDL) it.next());
				this.firstRendered = firstIndex;
				this.lastRendered = firstIndex - 1;
				addRow(row);
				this.lastRendered++;
				setContainerHeight();
				fixSpacers();
				while (it.hasNext()) {
					addRow(prepareRow((UIDL) it.next()));
					this.lastRendered++;
				}
				fixSpacers();
			}

			// this may be a new set of rows due content change,
			// ensure we have proper cache rows
			ensureCacheFilled();
		}

		/**
		 * Ensure we have the correct set of rows on client side, e.g. if the
		 * content on the server side has changed, or the client scroll position
		 * has changed since the last request.
		 */
		protected void ensureCacheFilled() {
			int reactFirstRow = (int) (VOrchidScrollTable.this.firstRowInViewPort - VOrchidScrollTable.this.pageLength
			        * VOrchidScrollTable.this.cache_react_rate);
			int reactLastRow = (int) (VOrchidScrollTable.this.firstRowInViewPort + VOrchidScrollTable.this.pageLength + VOrchidScrollTable.this.pageLength
			        * VOrchidScrollTable.this.cache_react_rate);
			if (reactFirstRow < 0) {
				reactFirstRow = 0;
			}
			if (reactLastRow >= VOrchidScrollTable.this.totalRows) {
				reactLastRow = VOrchidScrollTable.this.totalRows - 1;
			}
			if (this.lastRendered < reactFirstRow || this.firstRendered > reactLastRow) {
				/*
				 * #8040 - scroll position is completely changed since the
				 * latest request, so request a new set of rows. TODO: We should
				 * probably check whether the fetched rows match the current
				 * scroll position right when they arrive, so as to not waste
				 * time rendering a set of rows that will never be visible...
				 */
				VOrchidScrollTable.this.rowRequestHandler.setReqFirstRow(reactFirstRow);
				VOrchidScrollTable.this.rowRequestHandler.setReqRows(reactLastRow - reactFirstRow + 1);
				VOrchidScrollTable.this.rowRequestHandler.deferRowFetch(1);
			} else if (this.lastRendered < reactLastRow) {
				// get some cache rows below visible area
				VOrchidScrollTable.this.rowRequestHandler.setReqFirstRow(this.lastRendered + 1);
				VOrchidScrollTable.this.rowRequestHandler.setReqRows(reactLastRow - this.lastRendered);
				VOrchidScrollTable.this.rowRequestHandler.deferRowFetch(1);
			} else if (this.firstRendered > reactFirstRow) {
				/*
				 * Branch for fetching cache above visible area. If cache needed
				 * for both before and after visible area, this will be rendered
				 * after-cache is received and rendered. So in some rare
				 * situations the table may make two cache visits to server.
				 */
				VOrchidScrollTable.this.rowRequestHandler.setReqFirstRow(reactFirstRow);
				VOrchidScrollTable.this.rowRequestHandler.setReqRows(this.firstRendered - reactFirstRow);
				VOrchidScrollTable.this.rowRequestHandler.deferRowFetch(1);
			}
		}

		/**
		 * Inserts rows as provided in the rowData starting at firstIndex.
		 * 
		 * @param rowData
		 * @param firstIndex
		 * @param rows
		 *            the number of rows
		 * @return a list of the rows added.
		 */
		protected List<VScrollTableRow> insertRows(UIDL rowData, int firstIndex, int rows) {
			this.aligns = VOrchidScrollTable.this.tHead.getColumnAlignments();
			final Iterator<?> it = rowData.getChildIterator();
			List<VScrollTableRow> insertedRows = new ArrayList<VScrollTableRow>();

			if (firstIndex == this.lastRendered + 1) {
				while (it.hasNext()) {
					final VScrollTableRow row = prepareRow((UIDL) it.next());
					addRow(row);
					insertedRows.add(row);
					this.lastRendered++;
				}
				fixSpacers();
			} else if (firstIndex + rows == this.firstRendered) {
				final VScrollTableRow[] rowArray = new VScrollTableRow[rows];
				int i = rows;
				while (it.hasNext()) {
					i--;
					rowArray[i] = prepareRow((UIDL) it.next());
				}
				for (i = 0; i < rows; i++) {
					addRowBeforeFirstRendered(rowArray[i]);
					insertedRows.add(rowArray[i]);
					this.firstRendered--;
				}
			} else {
				// insert in the middle
				int ix = firstIndex;
				while (it.hasNext()) {
					VScrollTableRow row = prepareRow((UIDL) it.next());
					insertRowAt(row, ix);
					insertedRows.add(row);
					this.lastRendered++;
					ix++;
				}
				fixSpacers();
			}
			return insertedRows;
		}

		protected List<VScrollTableRow> insertAndReindexRows(UIDL rowData, int firstIndex, int rows) {
			List<VScrollTableRow> inserted = insertRows(rowData, firstIndex, rows);
			int actualIxOfFirstRowAfterInserted = firstIndex + rows - this.firstRendered;
			for (int ix = actualIxOfFirstRowAfterInserted; ix < this.renderedRows.size(); ix++) {
				VScrollTableRow r = (VScrollTableRow) this.renderedRows.get(ix);
				r.setIndex(r.getIndex() + rows);
			}
			setContainerHeight();
			return inserted;
		}

		protected void insertRowsDeleteBelow(UIDL rowData, int firstIndex, int rows) {
			unlinkAllRowsStartingAt(firstIndex);
			insertRows(rowData, firstIndex, rows);
			setContainerHeight();
		}

		/**
		 * This method is used to instantiate new rows for this table. It
		 * automatically sets correct widths to rows cells and assigns correct
		 * client reference for child widgets.
		 * 
		 * This method can be called only after table has been initialized
		 * 
		 * @param uidl
		 */
		private VScrollTableRow prepareRow(UIDL uidl) {
			final VScrollTableRow row = createRow(uidl, this.aligns);
			row.initCellWidths();
			return row;
		}

		protected VScrollTableRow createRow(UIDL uidl, char[] aligns2) {
			if (uidl.hasAttribute("gen_html")) {
				// This is a generated row.
				return new VScrollTableGeneratedRow(uidl, aligns2);
			}
			return new VScrollTableRow(uidl, aligns2);
		}

		private void addRowBeforeFirstRendered(VScrollTableRow row) {
			row.setIndex(this.firstRendered - 1);
			if (row.isSelected()) {
				row.addStyleName("v-selected");
			}
			this.tBodyElement.insertBefore(row.getElement(), this.tBodyElement.getFirstChild());
			adopt(row);
			this.renderedRows.add(0, row);
		}

		private void addRow(VScrollTableRow row) {
			row.setIndex(this.firstRendered + this.renderedRows.size());
			if (row.isSelected()) {
				row.addStyleName("v-selected");
			}
			this.tBodyElement.appendChild(row.getElement());
			// Add to renderedRows before adopt so iterator() will return also
			// this row if called in an attach handler (#9264)
			this.renderedRows.add(row);
			adopt(row);
		}

		private void insertRowAt(VScrollTableRow row, int index) {
			row.setIndex(index);
			if (row.isSelected()) {
				row.addStyleName("v-selected");
			}
			if (index > 0) {
				VScrollTableRow sibling = getRowByRowIndex(index - 1);
				this.tBodyElement.insertAfter(row.getElement(), sibling.getElement());
			} else {
				VScrollTableRow sibling = getRowByRowIndex(index);
				this.tBodyElement.insertBefore(row.getElement(), sibling.getElement());
			}
			adopt(row);
			int actualIx = index - this.firstRendered;
			this.renderedRows.add(actualIx, row);
		}

		@Override
		public Iterator<Widget> iterator() {
			return this.renderedRows.iterator();
		}

		/**
		 * @return false if couldn't remove row
		 */
		protected boolean unlinkRow(boolean fromBeginning) {
			if (this.lastRendered - this.firstRendered < 0) {
				return false;
			}
			int actualIx;
			if (fromBeginning) {
				actualIx = 0;
				this.firstRendered++;
			} else {
				actualIx = this.renderedRows.size() - 1;
				this.lastRendered--;
			}
			if (actualIx >= 0) {
				unlinkRowAtActualIndex(actualIx);
				fixSpacers();
				return true;
			}
			return false;
		}

		protected void unlinkRows(int firstIndex, int count) {
			if (count < 1) {
				return;
			}
			if (this.firstRendered > firstIndex && this.firstRendered < firstIndex + count) {
				firstIndex = this.firstRendered;
			}
			int lastIndex = firstIndex + count - 1;
			if (this.lastRendered < lastIndex) {
				lastIndex = this.lastRendered;
			}
			for (int ix = lastIndex; ix >= firstIndex; ix--) {
				unlinkRowAtActualIndex(actualIndex(ix));
				this.lastRendered--;
			}
			fixSpacers();
		}

		protected void unlinkAndReindexRows(int firstIndex, int count) {
			unlinkRows(firstIndex, count);
			int actualFirstIx = firstIndex - this.firstRendered;
			for (int ix = actualFirstIx; ix < this.renderedRows.size(); ix++) {
				VScrollTableRow r = (VScrollTableRow) this.renderedRows.get(ix);
				r.setIndex(r.getIndex() - count);
			}
			setContainerHeight();
		}

		protected void unlinkAllRowsStartingAt(int index) {
			if (this.firstRendered > index) {
				index = this.firstRendered;
			}
			for (int ix = this.renderedRows.size() - 1; ix >= index; ix--) {
				unlinkRowAtActualIndex(actualIndex(ix));
				this.lastRendered--;
			}
			fixSpacers();
		}

		private int actualIndex(int index) {
			return index - this.firstRendered;
		}

		private void unlinkRowAtActualIndex(int index) {
			final VScrollTableRow toBeRemoved = (VScrollTableRow) this.renderedRows.get(index);
			// Unregister row tooltip
			VOrchidScrollTable.this.client.registerTooltip(VOrchidScrollTable.this, toBeRemoved.getElement(), null);
			for (int i = 0; i < toBeRemoved.getElement().getChildCount(); i++) {
				// Unregister cell tooltips
				Element td = toBeRemoved.getElement().getChild(i).cast();
				VOrchidScrollTable.this.client.registerTooltip(VOrchidScrollTable.this, td, null);
			}
			VOrchidScrollTable.this.lazyUnregistryBag.add(toBeRemoved);
			this.tBodyElement.removeChild(toBeRemoved.getElement());
			orphan(toBeRemoved);
			this.renderedRows.remove(index);
		}

		@Override
		public boolean remove(Widget w) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected void onAttach() {
			super.onAttach();
			setContainerHeight();
		}

		/**
		 * Fix container blocks height according to totalRows to avoid
		 * "bouncing" when scrolling
		 */
		private void setContainerHeight() {
			fixSpacers();
			DOM.setStyleAttribute(this.container, "height", measureRowHeightOffset(VOrchidScrollTable.this.totalRows) + "px");
		}

		private void fixSpacers() {
			int prepx = measureRowHeightOffset(this.firstRendered);
			if (prepx < 0) {
				prepx = 0;
			}
			this.preSpacer.getStyle().setPropertyPx("height", prepx);
			int postpx = measureRowHeightOffset(VOrchidScrollTable.this.totalRows - 1) - measureRowHeightOffset(this.lastRendered);
			if (postpx < 0) {
				postpx = 0;
			}
			this.postSpacer.getStyle().setPropertyPx("height", postpx);
		}

		public double getRowHeight() {
			return getRowHeight(false);
		}

		public double getRowHeight(boolean forceUpdate) {
			if (this.tBodyMeasurementsDone && !forceUpdate) {
				return this.rowHeight;
			} else {
				if (this.tBodyElement.getRows().getLength() > 0) {
					int tableHeight = getTableHeight();
					int rowCount = this.tBodyElement.getRows().getLength();
					this.rowHeight = tableHeight / (double) rowCount;
				} else {
					// Special cases if we can't just measure the current rows
					if (!Double.isNaN(VOrchidScrollTable.this.lastKnownRowHeight)) {
						// Use previous value if available
						if (BrowserInfo.get().isIE()) {
							/*
							 * IE needs to reflow the table element at this
							 * point to work correctly (e.g.
							 * com.vaadin.tests.components.table.
							 * ContainerSizeChange) - the other code paths
							 * already trigger reflows, but here it must be done
							 * explicitly.
							 */
							getTableHeight();
						}
						this.rowHeight = VOrchidScrollTable.this.lastKnownRowHeight;
					} else if (isAttached()) {
						// measure row height by adding a dummy row
						VScrollTableRow scrollTableRow = new VScrollTableRow();
						this.tBodyElement.appendChild(scrollTableRow.getElement());
						getRowHeight(forceUpdate);
						this.tBodyElement.removeChild(scrollTableRow.getElement());
					} else {
						// TODO investigate if this can never happen anymore
						return DEFAULT_ROW_HEIGHT;
					}
				}
				VOrchidScrollTable.this.lastKnownRowHeight = this.rowHeight;
				this.tBodyMeasurementsDone = true;
				return this.rowHeight;
			}
		}

		public int getTableHeight() {
			return this.table.getOffsetHeight();
		}

		/**
		 * Returns the width available for column content.
		 * 
		 * @param columnIndex
		 * @return
		 */
		public int getColWidth(int columnIndex) {
			if (this.tBodyMeasurementsDone) {
				if (this.renderedRows.isEmpty()) {
					// no rows yet rendered
					return 0;
				}
				for (Widget row : this.renderedRows) {
					if (!(row instanceof VScrollTableGeneratedRow)) {
						TableRowElement tr = row.getElement().cast();
						Element wrapperdiv = tr.getCells().getItem(columnIndex).getFirstChildElement().cast();
						return wrapperdiv.getOffsetWidth();
					}
				}
				return 0;
			} else {
				return 0;
			}
		}

		/**
		 * Sets the content width of a column.
		 * 
		 * Due IE limitation, we must set the width to a wrapper elements inside
		 * table cells (with overflow hidden, which does not work on td
		 * elements).
		 * 
		 * To get this work properly crossplatform, we will also set the width
		 * of td.
		 * 
		 * @param colIndex
		 * @param w
		 */
		public void setColWidth(int colIndex, int w) {
			for (Widget row : this.renderedRows) {
				((VScrollTableRow) row).setCellWidth(colIndex, w);
			}
		}

		private int cellExtraWidth = -1;

		/**
		 * Method to return the space used for cell paddings + border.
		 */
		private int getCellExtraWidth() {
			if (this.cellExtraWidth < 0) {
				detectExtrawidth();
			}
			return this.cellExtraWidth;
		}

		private void detectExtrawidth() {
			NodeList<TableRowElement> rows = this.tBodyElement.getRows();
			if (rows.getLength() == 0) {
				/* need to temporary add empty row and detect */
				VScrollTableRow scrollTableRow = new VScrollTableRow();
				this.tBodyElement.appendChild(scrollTableRow.getElement());
				detectExtrawidth();
				this.tBodyElement.removeChild(scrollTableRow.getElement());
			} else {
				boolean noCells = false;
				TableRowElement item = rows.getItem(0);
				TableCellElement firstTD = item.getCells().getItem(0);
				if (firstTD == null) {
					// content is currently empty, we need to add a fake cell
					// for measuring
					noCells = true;
					VScrollTableRow next = (VScrollTableRow) iterator().next();
					boolean sorted = VOrchidScrollTable.this.tHead.getHeaderCell(0) != null ? VOrchidScrollTable.this.tHead.getHeaderCell(0).isSorted() : false;
					next.addCell(null, "", ALIGN_LEFT, "", true, sorted);
					firstTD = item.getCells().getItem(0);
				}
				com.google.gwt.dom.client.Element wrapper = firstTD.getFirstChildElement();
				this.cellExtraWidth = firstTD.getOffsetWidth() - wrapper.getOffsetWidth();
				if (noCells) {
					firstTD.getParentElement().removeChild(firstTD);
				}
			}
		}

		private void reLayoutComponents() {
			for (Widget w : this) {
				VScrollTableRow r = (VScrollTableRow) w;
				for (Widget widget : r) {
					VOrchidScrollTable.this.client.handleComponentRelativeSize(widget);
				}
			}
		}

		public int getLastRendered() {
			return this.lastRendered;
		}

		public int getFirstRendered() {
			return this.firstRendered;
		}

		public void moveCol(int oldIndex, int newIndex) {

			// loop all rows and move given index to its new place
			final Iterator<?> rows = iterator();
			while (rows.hasNext()) {
				final VScrollTableRow row = (VScrollTableRow) rows.next();

				final Element td = DOM.getChild(row.getElement(), oldIndex);
				if (td != null) {
					DOM.removeChild(row.getElement(), td);

					DOM.insertChild(row.getElement(), td, newIndex);
				}
			}

		}

		/**
		 * Restore row visibility which is set to "none" when the row is
		 * rendered (due a performance optimization).
		 */
		private void restoreRowVisibility() {
			for (Widget row : this.renderedRows) {
				row.getElement().getStyle().setProperty("visibility", "");
			}
		}

		public class VScrollTableRow extends Panel implements ActionOwner, Container {

			private static final int TOUCHSCROLL_TIMEOUT = 1000; // TODO: Change
			                                                     // this to
			                                                     // finetune
			                                                     // touchscroll
			                                                     // timeout
			private static final int DRAGMODE_MULTIROW = 2;
			protected ArrayList<Widget> childWidgets = new ArrayList<Widget>();
			private boolean selected = false;
			protected final int rowKey;
			private List<UIDL> pendingComponentPaints;

			private String[] actionKeys = null;
			private final TableRowElement rowElement;
			private int index;
			private Event touchStart;
			private static final String ROW_CLASSNAME_EVEN = CLASSNAME + "-row";
			private static final String ROW_CLASSNAME_ODD = CLASSNAME + "-row-odd";
			private static final int TOUCH_CONTEXT_MENU_TIMEOUT = 500;
			private Timer contextTouchTimeout;
			private Timer dragTouchTimeout;
			private int touchStartY;
			private int touchStartX;
			private boolean isDragging = false;

			private VScrollTableRow(int rowKey) {
				this.rowKey = rowKey;
				this.rowElement = Document.get().createTRElement();
				setElement(this.rowElement);
				DOM.sinkEvents(getElement(), Event.MOUSEEVENTS | Event.TOUCHEVENTS | Event.ONDBLCLICK | Event.ONCONTEXTMENU | VTooltip.TOOLTIP_EVENTS);
			}

			public VScrollTableRow(UIDL uidl, char[] aligns) {
				this(uidl.getIntAttribute("key"));

				/*
				 * Rendering the rows as hidden improves Firefox and Safari
				 * performance drastically.
				 */
				getElement().getStyle().setProperty("visibility", "hidden");

				String rowStyle = uidl.getStringAttribute("rowstyle");
				if (rowStyle != null) {
					addStyleName(CLASSNAME + "-row-" + rowStyle);
				}

				String rowDescription = uidl.getStringAttribute("rowdescr");
				if (rowDescription != null && !rowDescription.equals("")) {
					TooltipInfo info = new TooltipInfo(rowDescription);
					VOrchidScrollTable.this.client.registerTooltip(VOrchidScrollTable.this, this.rowElement, info);
				} else {
					// Remove possibly previously set tooltip
					VOrchidScrollTable.this.client.registerTooltip(VOrchidScrollTable.this, this.rowElement, null);
				}

				VOrchidScrollTable.this.tHead.getColumnAlignments();
				int col = 0;
				int visibleColumnIndex = -1;

				// row header
				if (VOrchidScrollTable.this.showRowHeaders) {
					boolean sorted = VOrchidScrollTable.this.tHead.getHeaderCell(col).isSorted();
					addCell(uidl, buildCaptionHtmlSnippet(uidl), aligns[col++], "rowheader", true, sorted);
					visibleColumnIndex++;
				}

				if (uidl.hasAttribute("al")) {
					this.actionKeys = uidl.getStringArrayAttribute("al");
				}

				addCellsFromUIDL(uidl, aligns, col, visibleColumnIndex);

				if (uidl.hasAttribute("selected") && !isSelected()) {
					toggleSelection();
				}
			}

			/**
			 * Add a dummy row, used for measurements if Table is empty.
			 */
			public VScrollTableRow() {
				this(0);
				addStyleName(CLASSNAME + "-row");
				addCell(null, "_", 'b', "", true, false);
			}

			protected void initCellWidths() {
				final int cells = VOrchidScrollTable.this.tHead.getVisibleCellCount();
				for (int i = 0; i < cells; i++) {
					int w = VOrchidScrollTable.this.getColWidth(getColKeyByIndex(i));
					if (w < 0) {
						w = 0;
					}
					setCellWidth(i, w);
				}
			}

			protected void setCellWidth(int cellIx, int width) {
				final Element cell = DOM.getChild(getElement(), cellIx);
				cell.getFirstChildElement().getStyle().setPropertyPx("width", width);
				cell.getStyle().setPropertyPx("width", width);
			}

			protected void addCellsFromUIDL(UIDL uidl, char[] aligns, int col, int visibleColumnIndex) {
				final Iterator<?> cells = uidl.getChildIterator();
				while (cells.hasNext()) {
					final Object cell = cells.next();
					visibleColumnIndex++;

					String columnId = VOrchidScrollTable.this.visibleColOrder[visibleColumnIndex];

					String style = "";
					if (uidl.hasAttribute("style-" + columnId)) {
						style = uidl.getStringAttribute("style-" + columnId);
					}

					String description = null;
					if (uidl.hasAttribute("descr-" + columnId)) {
						description = uidl.getStringAttribute("descr-" + columnId);
					}

					boolean sorted = VOrchidScrollTable.this.tHead.getHeaderCell(col).isSorted();
					if (cell instanceof String) {
						addCell(uidl, cell.toString(), aligns[col++], style, isRenderHtmlInCells(), sorted, description);
					} else {
						final Paintable cellContent = VOrchidScrollTable.this.client.getPaintable((UIDL) cell);

						addCell(uidl, (Widget) cellContent, aligns[col++], style, sorted);
						paintComponent(cellContent, (UIDL) cell);
					}
				}
			}

			/**
			 * Overriding this and returning true causes all text cells to be
			 * rendered as HTML.
			 * 
			 * @return always returns false in the default implementation
			 */
			protected boolean isRenderHtmlInCells() {
				return false;
			}

			/**
			 * Detects whether row is visible in tables viewport.
			 * 
			 * @return
			 */
			public boolean isInViewPort() {
				int absoluteTop = getAbsoluteTop();
				int scrollPosition = VOrchidScrollTable.this.scrollBodyPanel.getScrollPosition();
				if (absoluteTop < scrollPosition) {
					return false;
				}
				int maxVisible = scrollPosition + VOrchidScrollTable.this.scrollBodyPanel.getOffsetHeight() - getOffsetHeight();
				if (absoluteTop > maxVisible) {
					return false;
				}
				return true;
			}

			/**
			 * Makes a check based on indexes whether the row is before the
			 * compared row.
			 * 
			 * @param row1
			 * @return true if this rows index is smaller than in the row1
			 */
			public boolean isBefore(VScrollTableRow row1) {
				return getIndex() < row1.getIndex();
			}

			/**
			 * Sets the index of the row in the whole table. Currently used just
			 * to set even/odd classname
			 * 
			 * @param indexInWholeTable
			 */
			private void setIndex(int indexInWholeTable) {
				this.index = indexInWholeTable;
				boolean isOdd = indexInWholeTable % 2 == 0;
				// Inverted logic to be backwards compatible with earlier 6.4.
				// It is very strange because rows 1,3,5 are considered "even"
				// and 2,4,6 "odd".
				//
				// First remove any old styles so that both styles aren't
				// applied when indexes are updated.
				removeStyleName(ROW_CLASSNAME_ODD);
				removeStyleName(ROW_CLASSNAME_EVEN);
				if (!isOdd) {
					addStyleName(ROW_CLASSNAME_ODD);
				} else {
					addStyleName(ROW_CLASSNAME_EVEN);
				}
			}

			public int getIndex() {
				return this.index;
			}

			protected void paintComponent(Paintable p, UIDL uidl) {
				if (isAttached()) {
					p.updateFromUIDL(uidl, VOrchidScrollTable.this.client);
				} else {
					if (this.pendingComponentPaints == null) {
						this.pendingComponentPaints = new LinkedList<UIDL>();
					}
					this.pendingComponentPaints.add(uidl);
				}
			}

			@Override
			protected void onAttach() {
				super.onAttach();
				if (this.pendingComponentPaints != null) {
					for (UIDL uidl : this.pendingComponentPaints) {
						Paintable paintable = VOrchidScrollTable.this.client.getPaintable(uidl);
						paintable.updateFromUIDL(uidl, VOrchidScrollTable.this.client);
					}
					this.pendingComponentPaints.clear();
				}
			}

			@Override
			protected void onDetach() {
				super.onDetach();
				VOrchidScrollTable.this.client.getContextMenu().ensureHidden(this);
			}

			public String getKey() {
				return String.valueOf(this.rowKey);
			}

			public void addCell(UIDL rowUidl, String text, char align, String style, boolean textIsHTML, boolean sorted) {
				addCell(rowUidl, text, align, style, textIsHTML, sorted, null);
			}

			public void addCell(UIDL rowUidl, String text, char align, String style, boolean textIsHTML, boolean sorted, String description) {
				// String only content is optimized by not using Label widget
				final TableCellElement td = DOM.createTD().cast();
				initCellWithText(text, align, style, textIsHTML, sorted, description, td);
			}

			protected void initCellWithText(String text, char align, String style, boolean textIsHTML, boolean sorted, String description,
			        final TableCellElement td) {
				final Element container = DOM.createDiv();
				String className = CLASSNAME + "-cell-content";
				if (style != null && !style.equals("")) {
					className += " " + CLASSNAME + "-cell-content-" + style;
				}
				if (sorted) {
					className += " " + CLASSNAME + "-cell-content-sorted";
				}
				td.setClassName(className);
				container.setClassName(CLASSNAME + "-cell-wrapper");
				if (textIsHTML) {
					container.setInnerHTML(text);
				} else {
					container.setInnerText(text);
				}
				if (align != ALIGN_LEFT) {
					switch (align) {
						case ALIGN_CENTER:
							container.getStyle().setProperty("textAlign", "center");
							break;
						case ALIGN_RIGHT:
						default:
							container.getStyle().setProperty("textAlign", "right");
							break;
					}
				}

				if (description != null && !description.equals("")) {
					TooltipInfo info = new TooltipInfo(description);
					VOrchidScrollTable.this.client.registerTooltip(VOrchidScrollTable.this, td, info);
				} else {
					// Remove possibly previously set tooltip
					VOrchidScrollTable.this.client.registerTooltip(VOrchidScrollTable.this, td, null);
				}

				td.appendChild(container);
				getElement().appendChild(td);
			}

			public void addCell(UIDL rowUidl, Widget w, char align, String style, boolean sorted) {
				final TableCellElement td = DOM.createTD().cast();
				initCellWithWidget(w, align, style, sorted, td);
			}

			protected void initCellWithWidget(Widget w, char align, String style, boolean sorted, final TableCellElement td) {
				final Element container = DOM.createDiv();
				String className = CLASSNAME + "-cell-content";
				if (style != null && !style.equals("")) {
					className += " " + CLASSNAME + "-cell-content-" + style;
				}
				if (sorted) {
					className += " " + CLASSNAME + "-cell-content-sorted";
				}
				td.setClassName(className);
				container.setClassName(CLASSNAME + "-cell-wrapper");
				// TODO most components work with this, but not all (e.g.
				// Select)
				// Old comment: make widget cells respect align.
				// text-align:center for IE, margin: auto for others
				if (align != ALIGN_LEFT) {
					switch (align) {
						case ALIGN_CENTER:
							container.getStyle().setProperty("textAlign", "center");
							break;
						case ALIGN_RIGHT:
						default:
							container.getStyle().setProperty("textAlign", "right");
							break;
					}
				}
				td.appendChild(container);
				getElement().appendChild(td);
				// ensure widget not attached to another element (possible tBody
				// change)
				w.removeFromParent();
				container.appendChild(w.getElement());
				adopt(w);
				this.childWidgets.add(w);
			}

			@Override
			public Iterator<Widget> iterator() {
				return this.childWidgets.iterator();
			}

			@Override
			public boolean remove(Widget w) {
				if (this.childWidgets.contains(w)) {
					orphan(w);
					DOM.removeChild(DOM.getParent(w.getElement()), w.getElement());
					this.childWidgets.remove(w);
					return true;
				} else {
					return false;
				}
			}

			/**
			 * If there are registered click listeners, sends a click event and
			 * returns true. Otherwise, does nothing and returns false.
			 * 
			 * @param event
			 * @param targetTdOrTr
			 * @param immediate
			 *            Whether the event is sent immediately
			 * @return Whether a click event was sent
			 */
			private boolean handleClickEvent(Event event, Element targetTdOrTr, boolean immediate) {
				if (!VOrchidScrollTable.this.client.hasEventListeners(VOrchidScrollTable.this, ITEM_CLICK_EVENT_ID)) {
					// Don't send an event if nobody is listening
					return false;
				}

				// This row was clicked
				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "clickedKey", "" + this.rowKey, false);

				if (getElement() == targetTdOrTr.getParentElement()) {
					// A specific column was clicked
					int childIndex = DOM.getChildIndex(getElement(), targetTdOrTr);
					String colKey = null;
					colKey = VOrchidScrollTable.this.tHead.getHeaderCell(childIndex).getColKey();
					VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "clickedColKey", colKey, false);
				}

				MouseEventDetails details = new MouseEventDetails(event);

				VOrchidScrollTable.this.client.updateVariable(VOrchidScrollTable.this.paintableId, "clickEvent", details.toString(), immediate);

				return true;
			}

			private void handleTooltips(final Event event, Element target) {
				if (target.hasTagName("TD")) {
					// Table cell (td)
					Element container = target.getFirstChildElement().cast();
					Element widget = container.getFirstChildElement().cast();

					boolean containsWidget = false;
					for (Widget w : this.childWidgets) {
						if (widget == w.getElement()) {
							containsWidget = true;
							break;
						}
					}

					if (!containsWidget) {
						// Only text nodes has tooltips
						if (VOrchidScrollTable.this.client.getTooltipTitleInfo(VOrchidScrollTable.this, target) != null) {
							// Cell has description, use it
							VOrchidScrollTable.this.client.handleTooltipEvent(event, VOrchidScrollTable.this, target);
						} else {
							// Cell might have row description, use row
							// description
							VOrchidScrollTable.this.client.handleTooltipEvent(event, VOrchidScrollTable.this, target.getParentElement());
						}
					}

				} else {
					// Table row (tr)
					VOrchidScrollTable.this.client.handleTooltipEvent(event, VOrchidScrollTable.this, target);
				}
			}

			/**
			 * Special handler for touch devices that support native scrolling
			 * 
			 * @return Whether the event was handled by this method.
			 */
			private boolean handleTouchEvent(final Event event) {

				boolean touchEventHandled = false;

				if (VOrchidScrollTable.this.enabled && hasNativeTouchScrolling) {
					final Element targetTdOrTr = getEventTargetTdOrTr(event);
					final int type = event.getTypeInt();

					switch (type) {
						case Event.ONTOUCHSTART:
							touchEventHandled = true;
							this.touchStart = event;
							this.isDragging = false;
							Touch touch = event.getChangedTouches().get(0);
							// save position to fields, touches in events are
							// same
							// instance during the operation.
							this.touchStartX = touch.getClientX();
							this.touchStartY = touch.getClientY();

							if (VOrchidScrollTable.this.dragmode != 0) {
								if (this.dragTouchTimeout == null) {
									this.dragTouchTimeout = new Timer() {
										@Override
										public void run() {
											if (VScrollTableRow.this.touchStart != null) {
												// Start a drag if a finger is
												// held
												// in place long enough, then
												// moved
												VScrollTableRow.this.isDragging = true;
											}
										}
									};
								}
								this.dragTouchTimeout.schedule(TOUCHSCROLL_TIMEOUT);
							}

							if (this.actionKeys != null) {
								if (this.contextTouchTimeout == null) {
									this.contextTouchTimeout = new Timer() {
										@Override
										public void run() {
											if (VScrollTableRow.this.touchStart != null) {
												// Open the context menu if
												// finger
												// is held in place long enough.
												showContextMenu(VScrollTableRow.this.touchStart);
												event.preventDefault();
												VScrollTableRow.this.touchStart = null;
											}
										}
									};
								}
								this.contextTouchTimeout.schedule(TOUCH_CONTEXT_MENU_TIMEOUT);
							}
							break;
						case Event.ONTOUCHMOVE:
							touchEventHandled = true;
							if (isSignificantMove(event)) {
								if (this.contextTouchTimeout != null) {
									// Moved finger before the context menu
									// timer
									// expired, so let the browser handle this
									// as a
									// scroll.
									this.contextTouchTimeout.cancel();
									this.contextTouchTimeout = null;
								}
								if (!this.isDragging && this.dragTouchTimeout != null) {
									// Moved finger before the drag timer
									// expired,
									// so let the browser handle this as a
									// scroll.
									this.dragTouchTimeout.cancel();
									this.dragTouchTimeout = null;
								}

								if (VOrchidScrollTable.this.dragmode != 0 && this.touchStart != null && this.isDragging) {
									event.preventDefault();
									event.stopPropagation();
									startRowDrag(this.touchStart, type, targetTdOrTr);
								}
								this.touchStart = null;
							}
							break;
						case Event.ONTOUCHEND:
						case Event.ONTOUCHCANCEL:
							touchEventHandled = true;
							if (this.contextTouchTimeout != null) {
								this.contextTouchTimeout.cancel();
							}
							if (this.dragTouchTimeout != null) {
								this.dragTouchTimeout.cancel();
							}
							if (this.touchStart != null) {
								event.preventDefault();
								event.stopPropagation();
								if (!BrowserInfo.get().isAndroid()) {
									Util.simulateClickFromTouchEvent(this.touchStart, this);
								}
								this.touchStart = null;
							}
							this.isDragging = false;
							break;
					}
				}
				return touchEventHandled;
			}

			/*
			 * React on click that occur on content cells only
			 */
			@Override
			public void onBrowserEvent(final Event event) {

				final boolean touchEventHandled = handleTouchEvent(event);

				if (VOrchidScrollTable.this.enabled && !touchEventHandled) {
					final int type = event.getTypeInt();
					final Element targetTdOrTr = getEventTargetTdOrTr(event);
					if (type == Event.ONCONTEXTMENU) {
						showContextMenu(event);
						if (VOrchidScrollTable.this.enabled
						        && (this.actionKeys != null || VOrchidScrollTable.this.client.hasEventListeners(VOrchidScrollTable.this, ITEM_CLICK_EVENT_ID))) {
							/*
							 * Prevent browser context menu only if there are
							 * action handlers or item click listeners
							 * registered
							 */
							event.stopPropagation();
							event.preventDefault();
						}
						return;
					}

					boolean targetCellOrRowFound = targetTdOrTr != null;
					if (targetCellOrRowFound) {
						handleTooltips(event, targetTdOrTr);
					}

					switch (type) {
						case Event.ONDBLCLICK:
							if (targetCellOrRowFound) {
								handleClickEvent(event, targetTdOrTr, true);
							}
							break;
						case Event.ONMOUSEUP:
							if (targetCellOrRowFound) {
								/*
								 * Queue here, send at the same time as the
								 * corresponding value change event - see #7127
								 */
								boolean clickEventSent = handleClickEvent(event, targetTdOrTr, false);

								if (event.getButton() == Event.BUTTON_LEFT && isSelectable()) {

									// Ctrl+Shift click
									if ((event.getCtrlKey() || event.getMetaKey()) && event.getShiftKey() && isMultiSelectModeDefault()) {
										toggleShiftSelection(false);
										setRowFocus(this);

										// Ctrl click
									} else if ((event.getCtrlKey() || event.getMetaKey()) && isMultiSelectModeDefault()) {
										boolean wasSelected = isSelected();
										toggleSelection();
										setRowFocus(this);
										/*
										 * next possible range select must start
										 * on this row
										 */
										VOrchidScrollTable.this.selectionRangeStart = this;
										if (wasSelected) {
											removeRowFromUnsentSelectionRanges(this);
										}

									} else if ((event.getCtrlKey() || event.getMetaKey()) && isSingleSelectMode()) {
										// Ctrl (or meta) click (Single
										// selection)
										if (!isSelected() || (isSelected() && VOrchidScrollTable.this.nullSelectionAllowed)) {

											if (!isSelected()) {
												deselectAll();
											}

											toggleSelection();
											setRowFocus(this);
										}

									} else if (event.getShiftKey() && isMultiSelectModeDefault()) {
										// Shift click
										toggleShiftSelection(true);

									} else {
										// click
										boolean currentlyJustThisRowSelected = VOrchidScrollTable.this.selectedRowKeys.size() == 1
										        && VOrchidScrollTable.this.selectedRowKeys.contains(getKey());

										if (!currentlyJustThisRowSelected) {
											if (isSingleSelectMode() || isMultiSelectModeDefault()) {
												/*
												 * For default multi select mode
												 * (ctrl/shift) and for single
												 * select mode we need to clear
												 * the previous selection before
												 * selecting a new one when the
												 * user clicks on a row. Only in
												 * multiselect/simple mode the
												 * old selection should remain
												 * after a normal click.
												 */
												deselectAll();
											}
											toggleSelection();
										} else if ((isSingleSelectMode() || isMultiSelectModeSimple()) && VOrchidScrollTable.this.nullSelectionAllowed) {
											toggleSelection();
										}/*
										 * else NOP to avoid excessive server
										 * visits (selection is removed with
										 * CTRL/META click)
										 */

										VOrchidScrollTable.this.selectionRangeStart = this;
										setRowFocus(this);
									}

									// Remove IE text selection hack
									if (BrowserInfo.get().isIE()) {
										((Element) event.getEventTarget().cast()).setPropertyJSO("onselectstart", null);
									}
									// Queue value change
									sendSelectedRows(false);
								}
								/*
								 * Send queued click and value change events if
								 * any If a click event is sent, send value
								 * change with it regardless of the immediate
								 * flag, see #7127
								 */
								if (VOrchidScrollTable.this.immediate || clickEventSent) {
									VOrchidScrollTable.this.client.sendPendingVariableChanges();
								}
							}
							break;
						case Event.ONTOUCHEND:
						case Event.ONTOUCHCANCEL:
							if (this.touchStart != null) {
								/*
								 * Touch has not been handled as neither context
								 * or drag start, handle it as a click.
								 */
								Util.simulateClickFromTouchEvent(this.touchStart, this);
								this.touchStart = null;
							}
							if (this.contextTouchTimeout != null) {
								this.contextTouchTimeout.cancel();
							}
							break;
						case Event.ONTOUCHMOVE:
							if (isSignificantMove(event)) {
								/*
								 * TODO figure out scroll delegate don't eat
								 * events if row is selected. Null check for
								 * active delegate is as a workaround.
								 */
								if (VOrchidScrollTable.this.dragmode != 0 && this.touchStart != null && (TouchScrollDelegate.getActiveScrollDelegate() == null)) {
									startRowDrag(this.touchStart, type, targetTdOrTr);
								}
								if (this.contextTouchTimeout != null) {
									this.contextTouchTimeout.cancel();
								}
								/*
								 * Avoid clicks and drags by clearing touch
								 * start flag.
								 */
								this.touchStart = null;
							}

							break;
						case Event.ONTOUCHSTART:
							this.touchStart = event;
							Touch touch = event.getChangedTouches().get(0);
							// save position to fields, touches in events are
							// same
							// isntance during the operation.
							this.touchStartX = touch.getClientX();
							this.touchStartY = touch.getClientY();
							/*
							 * Prevent simulated mouse events.
							 */
							this.touchStart.preventDefault();
							if (VOrchidScrollTable.this.dragmode != 0 || this.actionKeys != null) {
								new Timer() {
									@Override
									public void run() {
										TouchScrollDelegate activeScrollDelegate = TouchScrollDelegate.getActiveScrollDelegate();
										/*
										 * If there's a scroll delegate, check
										 * if we're actually scrolling and
										 * handle it. If no delegate, do nothing
										 * here and let the row handle potential
										 * drag'n'drop or context menu.
										 */
										if (activeScrollDelegate != null) {
											if (activeScrollDelegate.isMoved()) {
												/*
												 * Prevent the row from handling
												 * touch move/end events (the
												 * delegate handles those) and
												 * from doing drag'n'drop or
												 * opening a context menu.
												 */
												VScrollTableRow.this.touchStart = null;
											} else {
												/*
												 * Scrolling hasn't started, so
												 * cancel delegate and let the
												 * row handle potential
												 * drag'n'drop or context menu.
												 */
												activeScrollDelegate.stopScrolling();
											}
										}
									}
								}.schedule(TOUCHSCROLL_TIMEOUT);

								if (this.contextTouchTimeout == null && this.actionKeys != null) {
									this.contextTouchTimeout = new Timer() {
										@Override
										public void run() {
											if (VScrollTableRow.this.touchStart != null) {
												showContextMenu(VScrollTableRow.this.touchStart);
												VScrollTableRow.this.touchStart = null;
											}
										}
									};
								}
								if (this.contextTouchTimeout != null) {
									this.contextTouchTimeout.cancel();
									this.contextTouchTimeout.schedule(TOUCH_CONTEXT_MENU_TIMEOUT);
								}
							}
							break;
						case Event.ONMOUSEDOWN:
							if (targetCellOrRowFound) {
								setRowFocus(this);
								ensureFocus();
								if (VOrchidScrollTable.this.dragmode != 0 && (event.getButton() == NativeEvent.BUTTON_LEFT)) {
									startRowDrag(event, type, targetTdOrTr);

								} else if (event.getCtrlKey() || event.getShiftKey() || event.getMetaKey() && isMultiSelectModeDefault()) {

									// Prevent default text selection in Firefox
									event.preventDefault();

									// Prevent default text selection in IE
									if (BrowserInfo.get().isIE()) {
										((Element) event.getEventTarget().cast()).setPropertyJSO("onselectstart", getPreventTextSelectionIEHack());
									}

									event.stopPropagation();
								}
							}
							break;
						case Event.ONMOUSEOUT:
							break;
						default:
							break;
					}
				}
				super.onBrowserEvent(event);
			}

			private boolean isSignificantMove(Event event) {
				if (this.touchStart == null) {
					// no touch start
					return false;
				}
				/*
				 * TODO calculate based on real distance instead of separate
				 * axis checks
				 */
				Touch touch = event.getChangedTouches().get(0);
				if (Math.abs(touch.getClientX() - this.touchStartX) > TouchScrollDelegate.SIGNIFICANT_MOVE_THRESHOLD) {
					return true;
				}
				if (Math.abs(touch.getClientY() - this.touchStartY) > TouchScrollDelegate.SIGNIFICANT_MOVE_THRESHOLD) {
					return true;
				}
				return false;
			}

			/**
			 * Checks if the row represented by the row key has been selected
			 * 
			 * @param key
			 *            The generated row key
			 */
			private boolean rowKeyIsSelected(int rowKey) {
				// Check single selections
				if (VOrchidScrollTable.this.selectedRowKeys.contains("" + rowKey)) {
					return true;
				}

				// Check range selections
				for (SelectionRange r : VOrchidScrollTable.this.selectedRowRanges) {
					if (r.inRange(getRenderedRowByKey("" + rowKey))) {
						return true;
					}
				}
				return false;
			}

			protected void startRowDrag(Event event, final int type, Element targetTdOrTr) {
				VTransferable transferable = new VTransferable();
				transferable.setDragSource(VOrchidScrollTable.this);
				transferable.setData("itemId", "" + this.rowKey);
				NodeList<TableCellElement> cells = this.rowElement.getCells();
				for (int i = 0; i < cells.getLength(); i++) {
					if (cells.getItem(i).isOrHasChild(targetTdOrTr)) {
						HeaderCell headerCell = VOrchidScrollTable.this.tHead.getHeaderCell(i);
						transferable.setData("propertyId", headerCell.cid);
						break;
					}
				}

				VDragEvent ev = VDragAndDropManager.get().startDrag(transferable, event, true);
				if (VOrchidScrollTable.this.dragmode == DRAGMODE_MULTIROW && isMultiSelectModeAny() && rowKeyIsSelected(this.rowKey)) {

					// Create a drag image of ALL rows (ie6,7 has a different
					// DOM structure)
					if (BrowserInfo.get().isIE6() || BrowserInfo.get().isIE7()) {
						ev.createDragImage(VOrchidScrollTable.this.scrollBody.getElement(), true);
					} else {
						ev.createDragImage((Element) VOrchidScrollTable.this.scrollBody.tBodyElement.cast(), true);
					}

					// Hide rows which are not selected
					Element dragImage = ev.getDragImage();
					int i = 0;
					for (Iterator<Widget> iterator = VOrchidScrollTable.this.scrollBody.iterator(); iterator.hasNext();) {
						VScrollTableRow next = (VScrollTableRow) iterator.next();

						Element child;
						if (BrowserInfo.get().isIE6() || BrowserInfo.get().isIE7()) {
							child = (Element) dragImage.getChild(1).getChild(0).getChild(i++);
						} else {
							child = (Element) dragImage.getChild(i++);
						}

						if (!rowKeyIsSelected(next.rowKey)) {
							child.getStyle().setVisibility(Visibility.HIDDEN);
						}
					}
				} else {
					ev.createDragImage(getElement(), true);
				}
				if (type == Event.ONMOUSEDOWN) {
					event.preventDefault();
				}
				event.stopPropagation();
			}

			/**
			 * Finds the TD that the event interacts with. Returns null if the
			 * target of the event should not be handled. If the event target is
			 * the row directly this method returns the TR element instead of
			 * the TD.
			 * 
			 * @param event
			 * @return TD or TR element that the event targets (the actual event
			 *         target is this element or a child of it)
			 */
			private Element getEventTargetTdOrTr(Event event) {
				final Element eventTarget = event.getEventTarget().cast();
				Widget widget = Util.findWidget(eventTarget, null);
				final Element thisTrElement = getElement();

				if (widget != this) {
					/*
					 * This is a workaround to make Labels, read only TextFields
					 * and Embedded in a Table clickable (see #2688). It is
					 * really not a fix as it does not work with a custom read
					 * only components (not extending VLabel/VEmbedded).
					 */
					while (widget != null && widget.getParent() != this) {
						widget = widget.getParent();
					}

					if (!(widget instanceof VLabel) && !(widget instanceof VEmbedded) && !(widget instanceof VTextField && ((VTextField) widget).isReadOnly())) {
						return null;
					}
				}
				if (eventTarget == thisTrElement) {
					// This was a click on the TR element
					return thisTrElement;
				}

				// Iterate upwards until we find the TR element
				Element element = eventTarget;
				while (element != null && element.getParentElement().cast() != thisTrElement) {
					element = element.getParentElement().cast();
				}
				return element;
			}

			public void showContextMenu(Event event) {
				if (VOrchidScrollTable.this.enabled && this.actionKeys != null) {
					// Show context menu if there are registered action handlers
					int left = Util.getTouchOrMouseClientX(event);
					int top = Util.getTouchOrMouseClientY(event);
					top += Window.getScrollTop();
					left += Window.getScrollLeft();
					VOrchidScrollTable.this.contextMenu = new ContextMenuDetails(getKey(), left, top);
					VOrchidScrollTable.this.client.getContextMenu().showAt(this, left, top);
				}
			}

			/**
			 * Has the row been selected?
			 * 
			 * @return Returns true if selected, else false
			 */
			public boolean isSelected() {
				return this.selected;
			}

			/**
			 * Toggle the selection of the row
			 */
			public void toggleSelection() {
				this.selected = !this.selected;
				VOrchidScrollTable.this.selectionChanged = true;
				if (this.selected) {
					VOrchidScrollTable.this.selectedRowKeys.add(String.valueOf(this.rowKey));
					addStyleName("v-selected");
				} else {
					removeStyleName("v-selected");
					VOrchidScrollTable.this.selectedRowKeys.remove(String.valueOf(this.rowKey));
				}
			}

			/**
			 * Is called when a user clicks an item when holding SHIFT key down.
			 * This will select a new range from the last focused row
			 * 
			 * @param deselectPrevious
			 *            Should the previous selected range be deselected
			 */
			private void toggleShiftSelection(boolean deselectPrevious) {

				/*
				 * Ensures that we are in multiselect mode and that we have a
				 * previous selection which was not a deselection
				 */
				if (isSingleSelectMode()) {
					// No previous selection found
					deselectAll();
					toggleSelection();
					return;
				}

				// Set the selectable range
				VScrollTableRow endRow = this;
				VScrollTableRow startRow = VOrchidScrollTable.this.selectionRangeStart;
				if (startRow == null) {
					startRow = VOrchidScrollTable.this.focusedRow;
					// If start row is null then we have a multipage selection
					// from
					// above
					if (startRow == null) {
						startRow = (VScrollTableRow) VOrchidScrollTable.this.scrollBody.iterator().next();
						setRowFocus(endRow);
					}
				}
				// Deselect previous items if so desired
				if (deselectPrevious) {
					deselectAll();
				}

				// we'll ensure GUI state from top down even though selection
				// was the opposite way
				if (!startRow.isBefore(endRow)) {
					VScrollTableRow tmp = startRow;
					startRow = endRow;
					endRow = tmp;
				}
				SelectionRange range = new SelectionRange(startRow, endRow);

				for (Widget w : VOrchidScrollTable.this.scrollBody) {
					VScrollTableRow row = (VScrollTableRow) w;
					if (range.inRange(row)) {
						if (!row.isSelected()) {
							row.toggleSelection();
						}
						VOrchidScrollTable.this.selectedRowKeys.add(row.getKey());
					}
				}

				// Add range
				if (startRow != endRow) {
					VOrchidScrollTable.this.selectedRowRanges.add(range);
				}
			}

			/*
			 * (non-Javadoc)
			 * @see com.vaadin.terminal.gwt.client.ui.IActionOwner#getActions ()
			 */
			@Override
			public Action[] getActions() {
				if (this.actionKeys == null) {
					return new Action[] {};
				}
				final Action[] actions = new Action[this.actionKeys.length];
				for (int i = 0; i < actions.length; i++) {
					final String actionKey = this.actionKeys[i];
					final TreeAction a = new TreeAction(this, String.valueOf(this.rowKey), actionKey) {
						@Override
						public void execute() {
							super.execute();
							lazyRevertFocusToRow(VScrollTableRow.this);
						}
					};
					a.setCaption(getActionCaption(actionKey));
					a.setIconUrl(getActionIcon(actionKey));
					actions[i] = a;
				}
				return actions;
			}

			@Override
			public ApplicationConnection getClient() {
				return VOrchidScrollTable.this.client;
			}

			@Override
			public String getPaintableId() {
				return VOrchidScrollTable.this.paintableId;
			}

			@Override
			public RenderSpace getAllocatedSpace(Widget child) {
				int w = 0;
				int i = getColIndexOf(child);
				HeaderCell headerCell = VOrchidScrollTable.this.tHead.getHeaderCell(i);
				if (headerCell != null) {
					if (VOrchidScrollTable.this.initializedAndAttached) {
						w = headerCell.getWidth();
					} else {
						// header offset width is not absolutely correct value,
						// but a best guess (expecting similar content in all
						// columns ->
						// if one component is relative width so are others)
						w = headerCell.getOffsetWidth() - getCellExtraWidth();
					}
				}
				return new RenderSpace(w, 0) {
					@Override
					public int getHeight() {
						return (int) getRowHeight();
					}
				};
			}

			private int getColIndexOf(Widget child) {
				com.google.gwt.dom.client.Element widgetCell = child.getElement().getParentElement().getParentElement();
				NodeList<TableCellElement> cells = this.rowElement.getCells();
				for (int i = 0; i < cells.getLength(); i++) {
					if (cells.getItem(i) == widgetCell) {
						return i;
					}
				}
				return -1;
			}

			@Override
			public boolean hasChildComponent(Widget component) {
				return this.childWidgets.contains(component);
			}

			@Override
			public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
				com.google.gwt.dom.client.Element parentElement = oldComponent.getElement().getParentElement();
				int index = this.childWidgets.indexOf(oldComponent);
				oldComponent.removeFromParent();

				parentElement.appendChild(newComponent.getElement());
				this.childWidgets.add(index, newComponent);
				adopt(newComponent);

			}

			@Override
			public boolean requestLayout(Set<Paintable> children) {
				// row size should never change and system wouldn't event
				// survive as this is a kind of fake paitable
				return true;
			}

			@Override
			public void updateCaption(Paintable component, UIDL uidl) {
				// NOP, not rendered
			}

			@Override
			public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
				// Should never be called,
				// Component container interface faked here to get layouts
				// render properly
			}
		}

		protected class VScrollTableGeneratedRow extends VScrollTableRow {

			private boolean spanColumns;
			private boolean htmlContentAllowed;

			public VScrollTableGeneratedRow(UIDL uidl, char[] aligns) {
				super(uidl, aligns);
				addStyleName("v-table-generated-row");
			}

			public boolean isSpanColumns() {
				return this.spanColumns;
			}

			@Override
			protected void initCellWidths() {
				if (this.spanColumns) {
					setSpannedColumnWidthAfterDOMFullyInited();
				} else {
					super.initCellWidths();
				}
			}

			private void setSpannedColumnWidthAfterDOMFullyInited() {
				// Defer setting width on spanned columns to make sure that
				// they are added to the DOM before trying to calculate
				// widths.
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						if (VOrchidScrollTable.this.showRowHeaders) {
							setCellWidth(0, VOrchidScrollTable.this.tHead.getHeaderCell(0).getWidth());
							calcAndSetSpanWidthOnCell(1);
						} else {
							calcAndSetSpanWidthOnCell(0);
						}
					}
				});
			}

			@Override
			protected boolean isRenderHtmlInCells() {
				return this.htmlContentAllowed;
			}

			@Override
			protected void addCellsFromUIDL(UIDL uidl, char[] aligns, int col, int visibleColumnIndex) {
				this.htmlContentAllowed = uidl.getBooleanAttribute("gen_html");
				this.spanColumns = uidl.getBooleanAttribute("gen_span");

				final Iterator<?> cells = uidl.getChildIterator();
				if (this.spanColumns) {
					int colCount = uidl.getChildCount();
					if (cells.hasNext()) {
						final Object cell = cells.next();
						if (cell instanceof String) {
							addSpannedCell(uidl, cell.toString(), aligns[0], "", this.htmlContentAllowed, false, null, colCount);
						} else {
							addSpannedCell(uidl, (Widget) cell, aligns[0], "", false, colCount);
						}
					}
				} else {
					super.addCellsFromUIDL(uidl, aligns, col, visibleColumnIndex);
				}
			}

			private void addSpannedCell(UIDL rowUidl, Widget w, char align, String style, boolean sorted, int colCount) {
				TableCellElement td = DOM.createTD().cast();
				td.setColSpan(colCount);
				initCellWithWidget(w, align, style, sorted, td);
			}

			private void addSpannedCell(UIDL rowUidl, String text, char align, String style, boolean textIsHTML, boolean sorted, String description,
			        int colCount) {
				// String only content is optimized by not using Label widget
				final TableCellElement td = DOM.createTD().cast();
				td.setColSpan(colCount);
				initCellWithText(text, align, style, textIsHTML, sorted, description, td);
			}

			@Override
			protected void setCellWidth(int cellIx, int width) {
				if (isSpanColumns()) {
					if (VOrchidScrollTable.this.showRowHeaders) {
						if (cellIx == 0) {
							super.setCellWidth(0, width);
						} else {
							// We need to recalculate the spanning TDs width for
							// every cellIx in order to support column resizing.
							calcAndSetSpanWidthOnCell(1);
						}
					} else {
						// Same as above.
						calcAndSetSpanWidthOnCell(0);
					}
				} else {
					super.setCellWidth(cellIx, width);
				}
			}

			private void calcAndSetSpanWidthOnCell(final int cellIx) {
				int spanWidth = 0;
				for (int ix = (VOrchidScrollTable.this.showRowHeaders ? 1 : 0); ix < VOrchidScrollTable.this.tHead.getVisibleCellCount(); ix++) {
					spanWidth += VOrchidScrollTable.this.tHead.getHeaderCell(ix).getOffsetWidth();
				}
				Util.setWidthExcludingPaddingAndBorder((Element) getElement().getChild(cellIx), spanWidth, 13, false);
			}
		}

		/**
		 * Ensure the component has a focus.
		 * 
		 * TODO the current implementation simply always calls focus for the
		 * component. In case the Table at some point implements focus/blur
		 * listeners, this method needs to be evolved to conditionally call
		 * focus only if not currently focused.
		 */
		protected void ensureFocus() {
			if (!VOrchidScrollTable.this.hasFocus) {
				VOrchidScrollTable.this.scrollBodyPanel.setFocus(true);
			}
		}
	}

	/**
	 * Deselects all items
	 */
	public void deselectAll() {
		for (Widget w : this.scrollBody) {
			VScrollTableRow row = (VScrollTableRow) w;
			if (row.isSelected()) {
				row.toggleSelection();
			}
		}
		// still ensure all selects are removed from (not necessary rendered)
		this.selectedRowKeys.clear();
		this.selectedRowRanges.clear();
		// also notify server that it clears all previous selections (the client
		// side does not know about the invisible ones)
		instructServerToForgetPreviousSelections();
	}

	/**
	 * Used in multiselect mode when the client side knows that all selections
	 * are in the next request.
	 */
	private void instructServerToForgetPreviousSelections() {
		this.client.updateVariable(this.paintableId, "clearSelections", true, false);
	}

	/**
	 * Determines the pagelength when the table height is fixed.
	 */
	public void updatePageLength() {
		// Only update if visible and enabled
		if (!isVisible() || !this.enabled) {
			return;
		}

		if (this.scrollBody == null) {
			return;
		}

		if (this.height == null || this.height.equals("")) {
			return;
		}

		int rowHeight = (int) Math.round(this.scrollBody.getRowHeight());
		int bodyH = this.scrollBodyPanel.getOffsetHeight();
		int rowsAtOnce = bodyH / rowHeight;
		boolean anotherPartlyVisible = ((bodyH % rowHeight) != 0);
		if (anotherPartlyVisible) {
			rowsAtOnce++;
		}
		if (this.pageLength != rowsAtOnce) {
			this.pageLength = rowsAtOnce;
			this.client.updateVariable(this.paintableId, "pagelength", this.pageLength, false);

			if (!this.rendering) {
				int currentlyVisible = this.scrollBody.lastRendered - this.scrollBody.firstRendered;
				if (currentlyVisible < this.pageLength && currentlyVisible < this.totalRows) {
					// shake scrollpanel to fill empty space
					this.scrollBodyPanel.setScrollPosition(this.scrollTop + 1);
					this.scrollBodyPanel.setScrollPosition(this.scrollTop - 1);
				}
			}
		}

	}

	@Override
	public void setWidth(String width) {
		if (this.width.equals(width)) {
			return;
		}
		if (!isVisible()) {
			/*
			 * Do not update size when the table is hidden as all column widths
			 * will be set to zero and they won't be recalculated when the table
			 * is set visible again (until the size changes again)
			 */
			return;
		}

		this.width = width;
		if (width != null && !"".equals(width)) {
			super.setWidth(width);
			int innerPixels = getOffsetWidth() - getBorderWidth();
			if (innerPixels < 0) {
				innerPixels = 0;
			}
			setContentWidth(innerPixels);

			// readjust undefined width columns
			triggerLazyColumnAdjustment(false);

		} else {

			// Undefined width
			super.setWidth("");

			// Readjust size of table
			sizeInit();

			// readjust undefined width columns
			triggerLazyColumnAdjustment(false);
		}

		/*
		 * setting width may affect wheter the component has scrollbars -> needs
		 * scrolling or not
		 */
		setProperTabIndex();
	}

	private static final int LAZY_COLUMN_ADJUST_TIMEOUT = 300;

	private final Timer lazyAdjustColumnWidths = new Timer() {
		/**
		 * Check for column widths, and available width, to see if we can fix
		 * column widths "optimally". Doing this lazily to avoid expensive
		 * calculation when resizing is not yet finished.
		 */
		@Override
		public void run() {
			if (VOrchidScrollTable.this.scrollBody == null) {
				// Try again later if we get here before scrollBody has been
				// initalized
				triggerLazyColumnAdjustment(false);
				return;
			}

			Iterator<Widget> headCells = VOrchidScrollTable.this.tHead.iterator();
			int usedMinimumWidth = 0;
			int totalExplicitColumnsWidths = 0;
			float expandRatioDivider = 0;
			int colIndex = 0;
			while (headCells.hasNext()) {
				final HeaderCell hCell = (HeaderCell) headCells.next();
				if (hCell.isDefinedWidth()) {
					totalExplicitColumnsWidths += hCell.getWidth();
					usedMinimumWidth += hCell.getWidth();
				} else {
					usedMinimumWidth += hCell.getNaturalColumnWidth(colIndex);
					expandRatioDivider += hCell.getExpandRatio();
				}
				colIndex++;
			}

			int availW = VOrchidScrollTable.this.scrollBody.getAvailableWidth();
			// Hey IE, are you really sure about this?
			availW = VOrchidScrollTable.this.scrollBody.getAvailableWidth();
			int visibleCellCount = VOrchidScrollTable.this.tHead.getVisibleCellCount();
			int totalExtraWidth = VOrchidScrollTable.this.scrollBody.getCellExtraWidth() * visibleCellCount;
			if (willHaveScrollbars()) {
				totalExtraWidth += Util.getNativeScrollbarSize();
			}
			availW -= totalExtraWidth;
			int forceScrollBodyWidth = -1;

			int extraSpace = availW - usedMinimumWidth;
			if (extraSpace < 0) {
				if (getTotalRows() == 0) {
					/*
					 * Too wide header combined with no rows in the table. No
					 * horizontal scrollbars would be displayed because there's
					 * no rows that grows too wide causing the scrollBody
					 * container div to overflow. Must explicitely force a width
					 * to a scrollbar. (see #9187)
					 */
					forceScrollBodyWidth = usedMinimumWidth + totalExtraWidth;
				}
				extraSpace = 0;
			}

			if (forceScrollBodyWidth > 0) {
				VOrchidScrollTable.this.scrollBody.container.getStyle().setWidth(forceScrollBodyWidth, Unit.PX);
			} else {
				// Clear width that might have been set to force horizontal
				// scrolling if there are no rows
				VOrchidScrollTable.this.scrollBody.container.getStyle().clearWidth();
			}

			int totalUndefinedNaturalWidths = usedMinimumWidth - totalExplicitColumnsWidths;

			// we have some space that can be divided optimally
			HeaderCell hCell;
			colIndex = 0;
			headCells = VOrchidScrollTable.this.tHead.iterator();
			int checksum = 0;
			while (headCells.hasNext()) {
				hCell = (HeaderCell) headCells.next();
				if (!hCell.isDefinedWidth()) {
					int w = hCell.getNaturalColumnWidth(colIndex);
					int newSpace;
					if (expandRatioDivider > 0) {
						// divide excess space by expand ratios
						newSpace = Math.round((w + extraSpace * hCell.getExpandRatio() / expandRatioDivider));
					} else {
						if (totalUndefinedNaturalWidths != 0) {
							// divide relatively to natural column widths
							newSpace = Math.round(w + (float) extraSpace * (float) w / totalUndefinedNaturalWidths);
						} else {
							newSpace = w;
						}
					}
					checksum += newSpace;
					setColWidth(colIndex, newSpace, false);
				} else {
					checksum += hCell.getWidth();
				}
				colIndex++;
			}

			if (extraSpace > 0 && checksum != availW) {
				/*
				 * There might be in some cases a rounding error of 1px when
				 * extra space is divided so if there is one then we give the
				 * first undefined column 1 more pixel
				 */
				headCells = VOrchidScrollTable.this.tHead.iterator();
				colIndex = 0;
				while (headCells.hasNext()) {
					HeaderCell hc = (HeaderCell) headCells.next();
					if (!hc.isDefinedWidth()) {
						setColWidth(colIndex, hc.getWidth() + availW - checksum, false);
						break;
					}
					colIndex++;
				}
			}

			if ((VOrchidScrollTable.this.height == null || "".equals(VOrchidScrollTable.this.height))
			        && VOrchidScrollTable.this.totalRows == VOrchidScrollTable.this.pageLength) {
				// fix body height (may vary if lazy loading is offhorizontal
				// scrollbar appears/disappears)
				int bodyHeight = Util.getRequiredHeight(VOrchidScrollTable.this.scrollBody);
				boolean needsSpaceForHorizontalScrollbar = (availW < usedMinimumWidth);
				if (needsSpaceForHorizontalScrollbar) {
					bodyHeight += Util.getNativeScrollbarSize();
				}
				int heightBefore = getOffsetHeight();
				VOrchidScrollTable.this.scrollBodyPanel.setHeight(bodyHeight + "px");
				if (heightBefore != getOffsetHeight()) {
					Util.notifyParentOfSizeChange(VOrchidScrollTable.this, false);
				}
			}
			VOrchidScrollTable.this.scrollBody.reLayoutComponents();
			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					Util.runWebkitOverflowAutoFix(VOrchidScrollTable.this.scrollBodyPanel.getElement());
				}
			});

			forceRealignColumnHeaders();
		}

	};

	private void forceRealignColumnHeaders() {
		if (BrowserInfo.get().isIE()) {
			/*
			 * IE does not fire onscroll event if scroll position is reverted to
			 * 0 due to the content element size growth. Ensure headers are in
			 * sync with content manually. Safe to use null event as we don't
			 * actually use the event object in listener.
			 */
			onScroll(null);
		}
	}

	/**
	 * helper to set pixel size of head and body part
	 * 
	 * @param pixels
	 */
	private void setContentWidth(int pixels) {
		this.tHead.setWidth(pixels + "px");
		this.scrollBodyPanel.setWidth(pixels + "px");
		this.tFoot.setWidth(pixels + "px");
	}

	private int borderWidth = -1;

	/**
	 * @return border left + border right
	 */
	private int getBorderWidth() {
		if (this.borderWidth < 0) {
			this.borderWidth = Util.measureHorizontalPaddingAndBorder(this.scrollBodyPanel.getElement(), 2);
			if (this.borderWidth < 0) {
				this.borderWidth = 0;
			}
		}
		return this.borderWidth;
	}

	/**
	 * Ensures scrollable area is properly sized. This method is used when fixed
	 * size is used.
	 */
	private int containerHeight;

	private void setContainerHeight() {
		if (this.height != null && !"".equals(this.height)) {
			this.containerHeight = getOffsetHeight();
			this.containerHeight -= this.showColHeaders ? this.tHead.getOffsetHeight() : 0;
			this.containerHeight -= this.tFoot.getOffsetHeight();
			this.containerHeight -= getContentAreaBorderHeight();
			if (this.containerHeight < 0) {
				this.containerHeight = 0;
			}
			this.scrollBodyPanel.setHeight(this.containerHeight + "px");
		}
	}

	private int contentAreaBorderHeight = -1;
	private int scrollLeft;
	private int scrollTop;
	private VScrollTableDropHandler dropHandler;
	private boolean navKeyDown;
	private boolean multiselectPending;

	/**
	 * @return border top + border bottom of the scrollable area of table
	 */
	private int getContentAreaBorderHeight() {
		if (this.contentAreaBorderHeight < 0) {
			if (BrowserInfo.get().isIE7() || BrowserInfo.get().isIE6()) {
				this.contentAreaBorderHeight = Util.measureVerticalBorder(this.scrollBodyPanel.getElement());
			} else {
				DOM.setStyleAttribute(this.scrollBodyPanel.getElement(), "overflow", "hidden");
				int oh = this.scrollBodyPanel.getOffsetHeight();
				int ch = this.scrollBodyPanel.getElement().getPropertyInt("clientHeight");
				this.contentAreaBorderHeight = oh - ch;
				DOM.setStyleAttribute(this.scrollBodyPanel.getElement(), "overflow", "auto");
			}
		}
		return this.contentAreaBorderHeight;
	}

	@Override
	public void setHeight(String height) {
		if (height.equals(this.height)) {
			return;
		}
		this.height = height;
		super.setHeight(height);
		setContainerHeight();

		if (this.initializedAndAttached) {
			updatePageLength();
		}
		if (!this.rendering) {
			// Webkit may sometimes get an odd rendering bug (white space
			// between header and body), see bug #3875. Running
			// overflow hack here to shake body element a bit.
			// We must run the fix as a deferred command to prevent it from
			// overwriting the scroll position with an outdated value, see
			// #7607.
			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					Util.runWebkitOverflowAutoFix(VOrchidScrollTable.this.scrollBodyPanel.getElement());
				}
			});
		}

		triggerLazyColumnAdjustment(false);

		/*
		 * setting height may affect wheter the component has scrollbars ->
		 * needs scrolling or not
		 */
		setProperTabIndex();

	}

	/*
	 * Overridden due Table might not survive of visibility change (scroll pos
	 * lost). Example ITabPanel just set contained components invisible and back
	 * when changing tabs.
	 */
	@Override
	public void setVisible(boolean visible) {
		if (isVisible() != visible) {
			super.setVisible(visible);
			if (this.initializedAndAttached) {
				if (visible) {
					Scheduler.get().scheduleDeferred(new Command() {
						@Override
						public void execute() {
							VOrchidScrollTable.this.scrollBodyPanel.setScrollPosition(measureRowHeightOffset(VOrchidScrollTable.this.firstRowInViewPort));
						}
					});
				}
			}
		}
	}

	/**
	 * Helper function to build html snippet for column or row headers
	 * 
	 * @param uidl
	 *            possibly with values caption and icon
	 * @return html snippet containing possibly an icon + caption text
	 */
	protected String buildCaptionHtmlSnippet(UIDL uidl) {
		String s = uidl.hasAttribute("caption") ? uidl.getStringAttribute("caption") : "";
		if (uidl.hasAttribute("icon")) {
			s = "<img src=\"" + Util.escapeAttribute(this.client.translateVaadinUri(uidl.getStringAttribute("icon"))) + "\" alt=\"icon\" class=\"v-icon\">" + s;
		}
		return s;
	}

	/**
	 * This method has logic which rows needs to be requested from server when
	 * user scrolls
	 */
	@Override
	public void onScroll(ScrollEvent event) {
		this.scrollLeft = this.scrollBodyPanel.getElement().getScrollLeft();
		this.scrollTop = this.scrollBodyPanel.getScrollPosition();
		/*
		 * #6970 - IE sometimes fires scroll events for a detached table. FIXME
		 * initializedAndAttached should probably be renamed - its name doesn't
		 * seem to reflect its semantics. onDetach() doesn't set it to false,
		 * and changing that might break something else, so we need to check
		 * isAttached() separately.
		 */
		if (!this.initializedAndAttached || !isAttached()) {
			return;
		}
		if (!this.enabled) {
			this.scrollBodyPanel.setScrollPosition(measureRowHeightOffset(this.firstRowInViewPort));
			return;
		}

		this.rowRequestHandler.cancel();

		if (BrowserInfo.get().isSafari() && event != null && this.scrollTop == 0) {
			// due to the webkitoverflowworkaround, top may sometimes report 0
			// for webkit, although it really is not. Expecting to have the
			// correct
			// value available soon.
			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					onScroll(null);
				}
			});
			return;
		}

		// fix headers horizontal scrolling
		this.tHead.setHorizontalScrollPosition(this.scrollLeft);

		// fix footers horizontal scrolling
		this.tFoot.setHorizontalScrollPosition(this.scrollLeft);

		this.firstRowInViewPort = calcFirstRowInViewPort();
		if (this.firstRowInViewPort > this.totalRows - this.pageLength) {
			this.firstRowInViewPort = this.totalRows - this.pageLength;
		}

		int postLimit = (int) (this.firstRowInViewPort + (this.pageLength - 1) + this.pageLength * this.cache_react_rate);
		if (postLimit > this.totalRows - 1) {
			postLimit = this.totalRows - 1;
		}
		int preLimit = (int) (this.firstRowInViewPort - this.pageLength * this.cache_react_rate);
		if (preLimit < 0) {
			preLimit = 0;
		}
		final int lastRendered = this.scrollBody.getLastRendered();
		final int firstRendered = this.scrollBody.getFirstRendered();

		if (postLimit <= lastRendered && preLimit >= firstRendered) {
			// we're within no-react area, no need to request more rows
			// remember which firstvisible we requested, in case the server has
			// a differing opinion
			this.lastRequestedFirstvisible = this.firstRowInViewPort;
			this.client.updateVariable(this.paintableId, "firstvisible", this.firstRowInViewPort, false);
			return;
		}

		if (this.firstRowInViewPort - this.pageLength * this.cache_rate > lastRendered
		        || this.firstRowInViewPort + this.pageLength + this.pageLength * this.cache_rate < firstRendered) {
			// need a totally new set of rows
			this.rowRequestHandler.setReqFirstRow((this.firstRowInViewPort - (int) (this.pageLength * this.cache_rate)));
			int last = this.firstRowInViewPort + (int) (this.cache_rate * this.pageLength) + this.pageLength - 1;
			if (last >= this.totalRows) {
				last = this.totalRows - 1;
			}
			this.rowRequestHandler.setReqRows(last - this.rowRequestHandler.getReqFirstRow() + 1);
			this.rowRequestHandler.deferRowFetch();
			return;
		}
		if (preLimit < firstRendered) {
			// need some rows to the beginning of the rendered area
			this.rowRequestHandler.setReqFirstRow((int) (this.firstRowInViewPort - this.pageLength * this.cache_rate));
			this.rowRequestHandler.setReqRows(firstRendered - this.rowRequestHandler.getReqFirstRow());
			this.rowRequestHandler.deferRowFetch();

			return;
		}
		if (postLimit > lastRendered) {
			// need some rows to the end of the rendered area
			this.rowRequestHandler.setReqFirstRow(lastRendered + 1);
			this.rowRequestHandler.setReqRows((int) ((this.firstRowInViewPort + this.pageLength + this.pageLength * this.cache_rate) - lastRendered));
			this.rowRequestHandler.deferRowFetch();
		}
	}

	protected int calcFirstRowInViewPort() {
		return (int) Math.ceil(this.scrollTop / this.scrollBody.getRowHeight());
	}

	@Override
	public VScrollTableDropHandler getDropHandler() {
		return this.dropHandler;
	}

	private static class TableDDDetails {
		int overkey = -1;
		VerticalDropLocation dropLocation;
		String colkey;

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TableDDDetails) {
				TableDDDetails other = (TableDDDetails) obj;
				return this.dropLocation == other.dropLocation && this.overkey == other.overkey
				        && ((this.colkey != null && this.colkey.equals(other.colkey)) || (this.colkey == null && other.colkey == null));
			}
			return false;
		}

		// @Override
		// public int hashCode() {
		// return overkey;
		// }
	}

	public class VScrollTableDropHandler extends VAbstractDropHandler {

		private static final String ROWSTYLEBASE = "v-table-row-drag-";
		private TableDDDetails dropDetails;
		private TableDDDetails lastEmphasized;

		@Override
		public void dragEnter(VDragEvent drag) {
			updateDropDetails(drag);
			super.dragEnter(drag);
		}

		private void updateDropDetails(VDragEvent drag) {
			this.dropDetails = new TableDDDetails();
			Element elementOver = drag.getElementOver();

			VScrollTableRow row = Util.findWidget(elementOver, getRowClass());
			if (row != null) {
				this.dropDetails.overkey = row.rowKey;
				Element tr = row.getElement();
				Element element = elementOver;
				while (element != null && element.getParentElement() != tr) {
					element = (Element) element.getParentElement();
				}
				int childIndex = DOM.getChildIndex(tr, element);
				this.dropDetails.colkey = VOrchidScrollTable.this.tHead.getHeaderCell(childIndex).getColKey();
				this.dropDetails.dropLocation = DDUtil.getVerticalDropLocation(row.getElement(), drag.getCurrentGwtEvent(), 0.2);
			}

			drag.getDropDetails().put("itemIdOver", this.dropDetails.overkey + "");
			drag.getDropDetails().put("detail", this.dropDetails.dropLocation != null ? this.dropDetails.dropLocation.toString() : null);

		}

		private Class<? extends Widget> getRowClass() {
			// get the row type this way to make dd work in derived
			// implementations
			return VOrchidScrollTable.this.scrollBody.iterator().next().getClass();
		}

		@Override
		public void dragOver(VDragEvent drag) {
			TableDDDetails oldDetails = this.dropDetails;
			updateDropDetails(drag);
			if (!oldDetails.equals(this.dropDetails)) {
				deEmphasis();
				final TableDDDetails newDetails = this.dropDetails;
				VAcceptCallback cb = new VAcceptCallback() {
					@Override
					public void accepted(VDragEvent event) {
						if (newDetails.equals(VScrollTableDropHandler.this.dropDetails)) {
							dragAccepted(event);
						}
						/*
						 * Else new target slot already defined, ignore
						 */
					}
				};
				validate(cb, drag);
			}
		}

		@Override
		public void dragLeave(VDragEvent drag) {
			deEmphasis();
			super.dragLeave(drag);
		}

		@Override
		public boolean drop(VDragEvent drag) {
			deEmphasis();
			return super.drop(drag);
		}

		private void deEmphasis() {
			UIObject.setStyleName(getElement(), CLASSNAME + "-drag", false);
			if (this.lastEmphasized == null) {
				return;
			}
			for (Widget w : VOrchidScrollTable.this.scrollBody.renderedRows) {
				VScrollTableRow row = (VScrollTableRow) w;
				if (this.lastEmphasized != null && row.rowKey == this.lastEmphasized.overkey) {
					String stylename = ROWSTYLEBASE + this.lastEmphasized.dropLocation.toString().toLowerCase();
					VScrollTableRow.setStyleName(row.getElement(), stylename, false);
					this.lastEmphasized = null;
					return;
				}
			}
		}

		/**
		 * TODO needs different drop modes ?? (on cells, on rows), now only
		 * supports rows
		 */
		private void emphasis(TableDDDetails details) {
			deEmphasis();
			UIObject.setStyleName(getElement(), CLASSNAME + "-drag", true);
			// iterate old and new emphasized row
			for (Widget w : VOrchidScrollTable.this.scrollBody.renderedRows) {
				VScrollTableRow row = (VScrollTableRow) w;
				if (details != null && details.overkey == row.rowKey) {
					String stylename = ROWSTYLEBASE + details.dropLocation.toString().toLowerCase();
					VScrollTableRow.setStyleName(row.getElement(), stylename, true);
					this.lastEmphasized = details;
					return;
				}
			}
		}

		@Override
		protected void dragAccepted(VDragEvent drag) {
			emphasis(this.dropDetails);
		}

		@Override
		public Paintable getPaintable() {
			return VOrchidScrollTable.this;
		}

		@Override
		public ApplicationConnection getApplicationConnection() {
			return VOrchidScrollTable.this.client;
		}

	}

	protected VScrollTableRow getFocusedRow() {
		return this.focusedRow;
	}

	/**
	 * Moves the selection head to a specific row
	 * 
	 * @param row
	 *            The row to where the selection head should move
	 * @return Returns true if focus was moved successfully, else false
	 */
	protected boolean setRowFocus(VScrollTableRow row) {

		if (!isSelectable()) {
			return false;
		}

		// Remove previous selection
		if (this.focusedRow != null && this.focusedRow != row) {
			this.focusedRow.removeStyleName(CLASSNAME_SELECTION_FOCUS);
		}

		if (row != null) {

			// Apply focus style to new selection
			row.addStyleName(CLASSNAME_SELECTION_FOCUS);

			/*
			 * Trying to set focus on already focused row
			 */
			if (row == this.focusedRow) {
				return false;
			}

			// Set new focused row
			this.focusedRow = row;

			ensureRowIsVisible(row);

			return true;
		}

		return false;
	}

	/**
	 * Ensures that the row is visible
	 * 
	 * @param row
	 *            The row to ensure is visible
	 */
	private void ensureRowIsVisible(VScrollTableRow row) {
		if (BrowserInfo.get().isTouchDevice()) {
			// Skip due to android devices that have broken scrolltop will may
			// get odd scrolling here.
			return;
		}
		Util.scrollIntoViewVertically(row.getElement());
	}

	/**
	 * Handles the keyboard events handled by the table
	 * 
	 * @param event
	 *            The keyboard event received
	 * @return true iff the navigation event was handled
	 */
	protected boolean handleNavigation(int keycode, boolean ctrl, boolean shift) {
		if (keycode == KeyCodes.KEY_TAB || keycode == KeyCodes.KEY_SHIFT) {
			// Do not handle tab key
			return false;
		}

		// Down navigation
		if (!isSelectable() && keycode == getNavigationDownKey()) {
			this.scrollBodyPanel.setScrollPosition(this.scrollBodyPanel.getScrollPosition() + this.scrollingVelocity);
			return true;
		} else if (keycode == getNavigationDownKey()) {
			if (isMultiSelectModeAny() && moveFocusDown()) {
				selectFocusedRow(ctrl, shift);

			} else if (isSingleSelectMode() && !shift && moveFocusDown()) {
				selectFocusedRow(ctrl, shift);
			}
			return true;
		}

		// Up navigation
		if (!isSelectable() && keycode == getNavigationUpKey()) {
			this.scrollBodyPanel.setScrollPosition(this.scrollBodyPanel.getScrollPosition() - this.scrollingVelocity);
			return true;
		} else if (keycode == getNavigationUpKey()) {
			if (isMultiSelectModeAny() && moveFocusUp()) {
				selectFocusedRow(ctrl, shift);
			} else if (isSingleSelectMode() && !shift && moveFocusUp()) {
				selectFocusedRow(ctrl, shift);
			}
			return true;
		}

		if (keycode == getNavigationLeftKey()) {
			// Left navigation
			this.scrollBodyPanel.setHorizontalScrollPosition(this.scrollBodyPanel.getHorizontalScrollPosition() - this.scrollingVelocity);
			return true;

		} else if (keycode == getNavigationRightKey()) {
			// Right navigation
			this.scrollBodyPanel.setHorizontalScrollPosition(this.scrollBodyPanel.getHorizontalScrollPosition() + this.scrollingVelocity);
			return true;
		}

		// Select navigation
		if (isSelectable() && keycode == getNavigationSelectKey()) {
			if (isSingleSelectMode()) {
				boolean wasSelected = this.focusedRow.isSelected();
				deselectAll();
				if (!wasSelected || !this.nullSelectionAllowed) {
					this.focusedRow.toggleSelection();
				}
			} else {
				this.focusedRow.toggleSelection();
				removeRowFromUnsentSelectionRanges(this.focusedRow);
			}

			sendSelectedRows();
			return true;
		}

		// Page Down navigation
		if (keycode == getNavigationPageDownKey()) {
			if (isSelectable()) {
				/*
				 * If selectable we plagiate MSW behaviour: first scroll to the
				 * end of current view. If at the end, scroll down one page
				 * length and keep the selected row in the bottom part of
				 * visible area.
				 */
				if (!isFocusAtTheEndOfTable()) {
					VScrollTableRow lastVisibleRowInViewPort = this.scrollBody.getRowByRowIndex(this.firstRowInViewPort + getFullyVisibleRowCount() - 1);
					if (lastVisibleRowInViewPort != null && lastVisibleRowInViewPort != this.focusedRow) {
						// focused row is not at the end of the table, move
						// focus and select the last visible row
						setRowFocus(lastVisibleRowInViewPort);
						selectFocusedRow(ctrl, shift);
						sendSelectedRows();
					} else {
						int indexOfToBeFocused = this.focusedRow.getIndex() + getFullyVisibleRowCount();
						if (indexOfToBeFocused >= this.totalRows) {
							indexOfToBeFocused = this.totalRows - 1;
						}
						VScrollTableRow toBeFocusedRow = this.scrollBody.getRowByRowIndex(indexOfToBeFocused);

						if (toBeFocusedRow != null) {
							/*
							 * if the next focused row is rendered
							 */
							setRowFocus(toBeFocusedRow);
							selectFocusedRow(ctrl, shift);
							// TODO needs scrollintoview ?
							sendSelectedRows();
						} else {
							// scroll down by pixels and return, to wait for
							// new rows, then select the last item in the
							// viewport
							this.selectLastItemInNextRender = true;
							this.multiselectPending = shift;
							scrollByPagelenght(1);
						}
					}
				}
			} else {
				/* No selections, go page down by scrolling */
				scrollByPagelenght(1);
			}
			return true;
		}

		// Page Up navigation
		if (keycode == getNavigationPageUpKey()) {
			if (isSelectable()) {
				/*
				 * If selectable we plagiate MSW behaviour: first scroll to the
				 * end of current view. If at the end, scroll down one page
				 * length and keep the selected row in the bottom part of
				 * visible area.
				 */
				if (!isFocusAtTheBeginningOfTable()) {
					VScrollTableRow firstVisibleRowInViewPort = this.scrollBody.getRowByRowIndex(this.firstRowInViewPort);
					if (firstVisibleRowInViewPort != null && firstVisibleRowInViewPort != this.focusedRow) {
						// focus is not at the beginning of the table, move
						// focus and select the first visible row
						setRowFocus(firstVisibleRowInViewPort);
						selectFocusedRow(ctrl, shift);
						sendSelectedRows();
					} else {
						int indexOfToBeFocused = this.focusedRow.getIndex() - getFullyVisibleRowCount();
						if (indexOfToBeFocused < 0) {
							indexOfToBeFocused = 0;
						}
						VScrollTableRow toBeFocusedRow = this.scrollBody.getRowByRowIndex(indexOfToBeFocused);

						if (toBeFocusedRow != null) { // if the next focused row
							                          // is rendered
							setRowFocus(toBeFocusedRow);
							selectFocusedRow(ctrl, shift);
							// TODO needs scrollintoview ?
							sendSelectedRows();
						} else {
							// unless waiting for the next rowset already
							// scroll down by pixels and return, to wait for
							// new rows, then select the last item in the
							// viewport
							this.selectFirstItemInNextRender = true;
							this.multiselectPending = shift;
							scrollByPagelenght(-1);
						}
					}
				}
			} else {
				/* No selections, go page up by scrolling */
				scrollByPagelenght(-1);
			}

			return true;
		}

		// Goto start navigation
		if (keycode == getNavigationStartKey()) {
			this.scrollBodyPanel.setScrollPosition(0);
			if (isSelectable()) {
				if (this.focusedRow != null && this.focusedRow.getIndex() == 0) {
					return false;
				} else {
					VScrollTableRow rowByRowIndex = (VScrollTableRow) this.scrollBody.iterator().next();
					if (rowByRowIndex.getIndex() == 0) {
						setRowFocus(rowByRowIndex);
						selectFocusedRow(ctrl, shift);
						sendSelectedRows();
					} else {
						// first row of table will come in next row fetch
						if (ctrl) {
							this.focusFirstItemInNextRender = true;
						} else {
							this.selectFirstItemInNextRender = true;
							this.multiselectPending = shift;
						}
					}
				}
			}
			return true;
		}

		// Goto end navigation
		if (keycode == getNavigationEndKey()) {
			this.scrollBodyPanel.setScrollPosition(this.scrollBody.getOffsetHeight());
			if (isSelectable()) {
				final int lastRendered = this.scrollBody.getLastRendered();
				if (lastRendered + 1 == this.totalRows) {
					VScrollTableRow rowByRowIndex = this.scrollBody.getRowByRowIndex(lastRendered);
					if (this.focusedRow != rowByRowIndex) {
						setRowFocus(rowByRowIndex);
						selectFocusedRow(ctrl, shift);
						sendSelectedRows();
					}
				} else {
					if (ctrl) {
						this.focusLastItemInNextRender = true;
					} else {
						this.selectLastItemInNextRender = true;
						this.multiselectPending = shift;
					}
				}
			}
			return true;
		}

		return false;
	}

	private boolean isFocusAtTheBeginningOfTable() {
		return this.focusedRow.getIndex() == 0;
	}

	private boolean isFocusAtTheEndOfTable() {
		return this.focusedRow.getIndex() + 1 >= this.totalRows;
	}

	private int getFullyVisibleRowCount() {
		return (int) (this.scrollBodyPanel.getOffsetHeight() / this.scrollBody.getRowHeight());
	}

	private void scrollByPagelenght(int i) {
		int pixels = i * this.scrollBodyPanel.getOffsetHeight();
		int newPixels = this.scrollBodyPanel.getScrollPosition() + pixels;
		if (newPixels < 0) {
			newPixels = 0;
		} // else if too high, NOP (all know browsers accept illegally big
		  // values here)
		this.scrollBodyPanel.setScrollPosition(newPixels);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.event.dom.client.FocusHandler#onFocus(com.google.gwt.event
	 * .dom.client.FocusEvent)
	 */
	@Override
	public void onFocus(FocusEvent event) {
		if (isFocusable()) {
			this.hasFocus = true;

			// Focus a row if no row is in focus
			if (this.focusedRow == null) {
				focusRowFromBody();
			} else {
				setRowFocus(this.focusedRow);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.event.dom.client.BlurHandler#onBlur(com.google.gwt.event
	 * .dom.client.BlurEvent)
	 */
	@Override
	public void onBlur(BlurEvent event) {
		this.hasFocus = false;
		this.navKeyDown = false;

		if (BrowserInfo.get().isIE()) {
			// IE sometimes moves focus to a clicked table cell...
			Element focusedElement = Util.getIEFocusedElement();
			if (Util.getPaintableForElement(this.client, getParent(), focusedElement) == this) {
				// ..in that case, steal the focus back to the focus handler
				// but not if focus is in a child component instead (#7965)
				focus();
				return;
			}
		}

		if (isFocusable()) {
			// Unfocus any row
			setRowFocus(null);
		}
	}

	/**
	 * Removes a key from a range if the key is found in a selected range
	 * 
	 * @param key
	 *            The key to remove
	 */
	private void removeRowFromUnsentSelectionRanges(VScrollTableRow row) {
		Collection<SelectionRange> newRanges = null;
		for (Iterator<SelectionRange> iterator = this.selectedRowRanges.iterator(); iterator.hasNext();) {
			SelectionRange range = iterator.next();
			if (range.inRange(row)) {
				// Split the range if given row is in range
				Collection<SelectionRange> splitranges = range.split(row);
				if (newRanges == null) {
					newRanges = new ArrayList<SelectionRange>();
				}
				newRanges.addAll(splitranges);
				iterator.remove();
			}
		}
		if (newRanges != null) {
			this.selectedRowRanges.addAll(newRanges);
		}
	}

	/**
	 * Can the Table be focused?
	 * 
	 * @return True if the table can be focused, else false
	 */
	public boolean isFocusable() {
		if (this.scrollBody != null && this.enabled) {
			return !(!hasHorizontalScrollbar() && !hasVerticalScrollbar() && !isSelectable());
		}
		return false;
	}

	private boolean hasHorizontalScrollbar() {
		return this.scrollBody.getOffsetWidth() > this.scrollBodyPanel.getOffsetWidth();
	}

	private boolean hasVerticalScrollbar() {
		return this.scrollBody.getOffsetHeight() > this.scrollBodyPanel.getOffsetHeight();
	}

	/*
	 * (non-Javadoc)
	 * @see com.vaadin.terminal.gwt.client.Focusable#focus()
	 */
	@Override
	public void focus() {
		if (isFocusable()) {
			this.scrollBodyPanel.focus();
		}
	}

	/**
	 * Sets the proper tabIndex for scrollBodyPanel (the focusable elemen in the
	 * component).
	 * 
	 * If the component has no explicit tabIndex a zero is given (default
	 * tabbing order based on dom hierarchy) or -1 if the component does not
	 * need to gain focus. The component needs no focus if it has no scrollabars
	 * (not scrollable) and not selectable. Note that in the future shortcut
	 * actions may need focus.
	 * 
	 */
	private void setProperTabIndex() {
		int storedScrollTop = 0;
		int storedScrollLeft = 0;

		if (BrowserInfo.get().getOperaVersion() >= 11) {
			// Workaround for Opera scroll bug when changing tabIndex (#6222)
			storedScrollTop = this.scrollBodyPanel.getScrollPosition();
			storedScrollLeft = this.scrollBodyPanel.getHorizontalScrollPosition();
		}

		if (this.tabIndex == 0 && !isFocusable()) {
			this.scrollBodyPanel.setTabIndex(-1);
		} else {
			this.scrollBodyPanel.setTabIndex(this.tabIndex);
		}

		if (BrowserInfo.get().getOperaVersion() >= 11) {
			// Workaround for Opera scroll bug when changing tabIndex (#6222)
			this.scrollBodyPanel.setScrollPosition(storedScrollTop);
			this.scrollBodyPanel.setHorizontalScrollPosition(storedScrollLeft);
		}
	}

	public void startScrollingVelocityTimer() {
		if (this.scrollingVelocityTimer == null) {
			this.scrollingVelocityTimer = new Timer() {
				@Override
				public void run() {
					VOrchidScrollTable.this.scrollingVelocity++;
				}
			};
			this.scrollingVelocityTimer.scheduleRepeating(100);
		}
	}

	public void cancelScrollingVelocityTimer() {
		if (this.scrollingVelocityTimer != null) {
			// Remove velocityTimer if it exists and the Table is disabled
			this.scrollingVelocityTimer.cancel();
			this.scrollingVelocityTimer = null;
			this.scrollingVelocity = 10;
		}
	}

	/**
	 * 
	 * @param keyCode
	 * @return true if the given keyCode is used by the table for navigation
	 */
	private boolean isNavigationKey(int keyCode) {
		return keyCode == getNavigationUpKey() || keyCode == getNavigationLeftKey() || keyCode == getNavigationRightKey() || keyCode == getNavigationDownKey()
		        || keyCode == getNavigationPageUpKey() || keyCode == getNavigationPageDownKey() || keyCode == getNavigationEndKey()
		        || keyCode == getNavigationStartKey();
	}

	public void lazyRevertFocusToRow(final VScrollTableRow currentlyFocusedRow) {
		Scheduler.get().scheduleFinally(new ScheduledCommand() {
			@Override
			public void execute() {
				if (currentlyFocusedRow != null) {
					setRowFocus(currentlyFocusedRow);
				} else {
					VConsole.log("no row?");
					focusRowFromBody();
				}
				VOrchidScrollTable.this.scrollBody.ensureFocus();
			}
		});
	}

	@Override
	public Action[] getActions() {
		if (this.bodyActionKeys == null) {
			return new Action[] {};
		}
		final Action[] actions = new Action[this.bodyActionKeys.length];
		for (int i = 0; i < actions.length; i++) {
			final String actionKey = this.bodyActionKeys[i];
			Action bodyAction = new TreeAction(this, null, actionKey);
			bodyAction.setCaption(getActionCaption(actionKey));
			bodyAction.setIconUrl(getActionIcon(actionKey));
			actions[i] = bodyAction;
		}
		return actions;
	}

	@Override
	public ApplicationConnection getClient() {
		return this.client;
	}

	@Override
	public String getPaintableId() {
		return this.paintableId;
	}

	/**
	 * Add this to the element mouse down event by using element.setPropertyJSO
	 * ("onselectstart",applyDisableTextSelectionIEHack()); Remove it then again
	 * when the mouse is depressed in the mouse up event.
	 * 
	 * @return Returns the JSO preventing text selection
	 */
	private static native JavaScriptObject getPreventTextSelectionIEHack()
	/*-{
	        return function(){ return false; };
	}-*/;

	protected void triggerLazyColumnAdjustment(boolean now) {
		this.lazyAdjustColumnWidths.cancel();
		if (now) {
			this.lazyAdjustColumnWidths.run();
		} else {
			this.lazyAdjustColumnWidths.schedule(LAZY_COLUMN_ADJUST_TIMEOUT);
		}
	}

	private void debug(String msg) {
		if (this.enableDebug) {
			VConsole.error(msg);
		}
	}
}
