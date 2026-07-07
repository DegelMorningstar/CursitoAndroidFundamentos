# Contacts Manager — Design Document
**Android Mobile App · Material Design 3 · v1.0**

---

## 1. Overview

A modern Android contacts management app focused on simplicity, speed, and clarity. Inspired by WhatsApp's structural patterns, adapted with a blue/indigo palette.

**Target users:** People who frequently manage personal or professional contacts on their smartphones.

**Core principles:** Simplicity · Speed · Clarity · Consistency · Accessibility

---

## 2. Design System

### 2.1 Color Palette

| Token | Hex | Usage |
|---|---|---|
| `primary` | `#4361ee` | FAB, active nav, primary buttons, header title |
| `primaryContainer` | `#eef0ff` | Nav pill, avatar action buttons, search bar fill |
| `background` | `#f8f9fe` | App background |
| `surface` | `#ffffff` | Cards, app bars, list rows, bottom nav |
| `onSurface` | `#1a1c2e` | Primary text, screen titles |
| `onSurfaceVariant` | `#9098b1` | Secondary text, placeholders, inactive icons |
| `outline` | `#f0f2ff` | Dividers between rows/fields |
| `outlineVariant` | `#eef0ff` | Section borders, containers |
| `error` | `#dc2626` | Validation errors, delete actions |
| `errorContainer` | `#fef2f2` | Delete button background |

**Page background gradient** (behind the phone frame):
```
linear-gradient(155deg, #0f0f1e 0%, #1a1c3e 45%, #2a3480 100%)
```

### 2.2 Avatar Color Palette

Each contact gets a deterministic color based on a hash of their name:

| # | Hex | | # | Hex |
|---|---|---|---|---|
| 1 | `#e53935` Red | | 7 | `#43a047` Green |
| 2 | `#d81b60` Pink | | 8 | `#f4511e` Deep Orange |
| 3 | `#8e24aa` Purple | | 9 | `#6d4c41` Brown |
| 4 | `#5e35b1` Deep Purple | | 10 | `#00acc1` Cyan |
| 5 | `#3949ab` Indigo | | 11 | `#7cb342` Light Green |
| 6 | `#1e88e5` Blue | | 12 | `#00897b` Teal |

### 2.3 Typography

**Font:** Plus Jakarta Sans (Google Fonts)
**Weights used:** 400 Regular · 500 Medium · 600 SemiBold · 700 Bold

| Style | Size | Weight | Usage |
|---|---|---|---|
| App Title | 22px | 700 | "Contacts" header |
| Screen Title | 17px | 600 | Detail/Form screen header |
| Contact Name | 15px | 600 | List row primary text |
| Hero Name | 22px | 700 | Detail screen name |
| Body | 15px | 500 | Info card values |
| Body Small | 13px | 400 | Phone/email in list rows |
| Label | 11px | 700 | Field labels (UPPERCASE) |
| Caption | 11–12px | 500–600 | Nav labels, secondary info |

### 2.4 Spacing & Sizing

| Element | Value |
|---|---|
| Screen horizontal padding | 16px |
| App bar height | 56px |
| Search bar height | 44px |
| Search bar margin | 0 16px 12px |
| Contact row padding | 12px 16px |
| Contact row avatar size | 46×46px (r: 23px) |
| Detail avatar size | 88×88px (r: 44px) |
| Action button size | 52×52px (r: 26px) |
| FAB size | 56×56px (r: 28px) |
| Bottom nav height | 64px |
| Card border radius | 16px |
| Dialog border radius | 24px |
| Button border radius (primary) | 18px |
| Button border radius (action) | 12px |
| Info row height | ~68px |

### 2.5 Shadows & Elevation

| Element | Shadow |
|---|---|
| FAB | `0 4px 16px rgba(67,97,238,0.50)` |
| Detail avatar | `0 4px 20px rgba(0,0,0,0.12)` |
| Form card | `0 1px 4px rgba(26,28,46,0.06)` |
| Delete dialog | `0 8px 32px rgba(0,0,0,0.20)` |
| Snackbar | `0 6px 20px rgba(0,0,0,0.30)` |

---

## 3. Components

### 3.1 Contact Row

```
┌──────────────────────────────────────────┐
│  [Avatar 46px]  Full Name          [★]   │  height: ~70px
│                 Phone number             │  padding: 12px 16px
└──────────────────────────────────────────┘
```

- **Avatar:** Colored circle, 2-letter initials, white 700 text
- **Name:** 15px/600, `#1a1c2e`, overflow ellipsis
- **Phone:** 13px/400, `#9098b1`, overflow ellipsis
- **Star:** `#f59f00` filled when favorite; `#d1d5db` stroke when not
- **Divider:** 1px `#f0f2ff` bottom border
- **Click:** Row → open Contact Detail; Star button → toggle favorite (stopPropagation)

### 3.2 Search Bar

```
┌────────────────────────────────────────┐
│  🔍  Buscar contactos...         [✕]  │  height: 44px
└────────────────────────────────────────┘
```

- Background: `#f0f2ff`, border-radius: 28px
- Icon: search SVG, `#9098b1`
- Clear button: appears only when `searchQuery.length > 0`
- Controlled input: filters list in real-time on every keystroke

### 3.3 Avatar

- **Size variants:** 46px (list), 88px (detail hero)
- **Color:** deterministic hash of `first + last` name
- **Initials:** first letter of first name + first letter of last name, uppercase
- **Font:** 16px/700 (list), 32px/700 (detail)

### 3.4 Floating Action Button (FAB)

- Size: 56×56px, border-radius: 28px
- Color: `#4361ee`
- Icon: white + cross (24px)
- Shadow: `0 4px 16px rgba(67,97,238,0.50)`
- Position: `bottom: 80px; right: 20px` (above bottom nav)
- Animation: `fabIn` scale-up on mount

### 3.5 Bottom Navigation

Two tabs: **Contactos** and **Favoritos**

- Height: 64px, background: `#ffffff`, top border: 1px `#eef0ff`
- Active state: pill indicator (`#eef0ff`, 68×30px), blue icon + bold label
- Inactive state: gray icon (`#9098b1`) + regular label
- Active color: `#4361ee`

### 3.6 Action Buttons (Detail Screen)

Row of 4: Llamar · Mensaje · Email · Favorito

- Size: 52×52px circle, background: `#eef0ff`
- Icon: 22px, stroke `#4361ee`
- Label: 11px/500, `#5c6080`
- Favorite button: filled `#f59f00` star when active

### 3.7 Info Card Rows

Each row: icon container (40×40px `#eef0ff` circle) + label + value

- Label: 11px/600, `#9098b1`, uppercase, letter-spacing: 0.7px
- Value: 15px/500, `#1a1c2e`
- Divider: 1px `#f0f2ff` between rows

### 3.8 Delete Button

- Background: `#fef2f2`
- Border: 1px `#fecaca`
- Text + icon: `#dc2626`
- Height: 48px, border-radius: 12px
- Full width

### 3.9 Confirmation Dialog

- Backdrop: `rgba(26,28,46,0.60)`
- Card: white, 24px radius, `dialogPop` animation
- Icon: trash icon in `#fef2f2` circle
- Title: 18px/700, `#1a1c2e`
- Body: 14px/400, `#9098b1`, line-height 1.6
- Actions: Cancel (`#f0f2ff` bg) + Delete (`#dc2626` bg), both 48px height

### 3.10 Snackbar

- Background: `#1a1c2e`
- Text: 14px/500, white
- Border-radius: 12px
- Position: absolute bottom (80px on list, 24px on detail/form)
- Animation: `snackSlide` (slide up from below)
- Auto-dismiss after 3 seconds

### 3.11 Form Field

```
┌────────────────────────────────┐
│ FIELD LABEL *                  │  11px/700 uppercase
│ Input value or placeholder     │  15px/400
│ Error message (if invalid)     │  12px, #dc2626
└────────────────────────────────┘
```

- Grouped in a white card (16px radius)
- Dividers between fields: 1px `#f0f2ff`
- Focused: cursor visible; no outline override needed (controlled)

### 3.12 Empty States

Three variants, all centered with a tinted icon circle + title + body:

| State | Trigger | Icon |
|---|---|---|
| No contacts | contacts array empty | Person outline |
| No search results | query with 0 matches | Search outline |
| No favorites | favorites tab, 0 starred | Star outline |

---

## 4. Screens

### 4.1 Contact List Screen

**Layout:**
- App bar (56px): Title "Contacts" + overflow menu
- Search bar (44px + 12px margin): below app bar
- Scrollable list: flex:1, padding-bottom 88px (FAB clearance)
- FAB: absolute, bottom 80px / right 20px
- Bottom nav: absolute, bottom 0, height 64px

**States:**
1. **Populated** — contact rows, scrollable
2. **Empty (no contacts)** — centered illustration + CTA text
3. **Searching with results** — filtered rows
4. **Searching with no results** — empty state with query echo

**Nav tabs:**
- Tab 0 (Contactos): shows all contacts, filtered by search
- Tab 1 (Favoritos): shows only `favorite: true` contacts, no search

---

### 4.2 Contact Detail Screen

**Layout:**
- Header (56px): back arrow + "Contacto" + edit + share
- Scrollable content:
  - Hero card: large avatar + name + company + 4 action buttons
  - Info card: phone, email, company rows with icon circles
  - Delete button (full-width, bottom of scroll)

**Interactions:**
- Favorite button in hero card → toggles + reflects in list
- Edit icon → opens Edit Contact screen
- Share icon → snackbar toast
- Delete button → opens Confirmation Dialog

---

### 4.3 Add Contact Screen

**Header:** × close + "Nuevo contacto" + Guardar button

**Form fields (in card):**
1. Nombre * (required)
2. Apellido
3. Teléfono * (required, type=tel)
4. Correo (type=email)
5. Empresa

**Validation:**
- Triggered on Guardar tap
- Empty required fields show inline error messages in `#dc2626`
- Errors clear individually as the user types in that field

**On save:**
- New contact added to array, sorted alphabetically
- Navigate back to list
- Snackbar: "Contacto agregado ✓"

---

### 4.4 Edit Contact Screen

Identical to Add Contact, with:
- Header: "Editar contacto"
- Fields pre-filled from selected contact
- Delete button below form fields
- On save → navigate back to Detail, snackbar "Cambios guardados ✓"

---

### 4.5 Favorites Screen

Reuses the Contact List layout entirely. Accessed via bottom nav tab 1. Shows only `favorite: true` contacts. Search bar is hidden when on Favorites tab (tab switches clear the query).

---

## 5. User Flows

### Flow 1 — View list → Search → View detail
```
Contact List → [type in search bar] → Filtered results → [tap row] → Contact Detail
```

### Flow 2 — View list → Create contact
```
Contact List → [tap FAB +] → Add Contact form → [fill fields] → [tap Guardar] → Contact List (updated)
```

### Flow 3 — View detail → Edit contact
```
Contact Detail → [tap ✏️ edit icon] → Edit Contact form → [modify fields] → [tap Guardar] → Contact Detail (updated)
```

### Flow 4 — Toggle favorite from list
```
Contact List → [tap ★ star on any row] → star animates → favorite state toggles
```

### Flow 5 — View favorites → View detail
```
Bottom Nav [Favoritos] → Favorites list → [tap contact] → Contact Detail
```

### Flow 6 — Share contact
```
Contact Detail → [tap share icon] → Snackbar "Contacto compartido ↗"
```

### Flow 7 — Delete contact
```
Contact Detail → [tap "Eliminar contacto"] → Confirmation Dialog → [tap Eliminar] → Contact List
  OR
Edit Contact → [tap "Eliminar contacto"] → Confirmation Dialog → [tap Eliminar] → Contact List
```

---

## 6. Animations & Microinteractions

| Interaction | Animation | Duration | Easing |
|---|---|---|---|
| Screen transition | `slideR` (slide from right, fade in) | 200ms | ease |
| FAB mount | `fabIn` (scale 0.5→1, fade in) | 350ms | `cubic-bezier(0.34,1.56,0.64,1)` |
| Star toggle | `starPop` (scale 1→1.7→1, slight rotate) | 550ms | `cubic-bezier(0.34,1.56,0.64,1)` |
| Delete dialog | `dialogPop` (scale 0.88→1, fade) | 200ms | `cubic-bezier(0.34,1.56,0.64,1)` |
| Snackbar | `snackSlide` (translateY 16px→0, fade) | 250ms | ease |
| Empty states | `fadeUp` (translateY 10px→0, fade) | 300–400ms | ease |

---

## 7. Accessibility Notes

- Minimum touch target: 44×44px (all interactive elements)
- Text contrast: `#1a1c2e` on `#ffffff` → ratio ~17:1 (AAA)
- Secondary text: `#9098b1` on `#ffffff` → ratio ~3.2:1 (AA Large)
- Primary blue `#4361ee` on white → ratio ~4.6:1 (AA)
- `pointer-events: none` on avatar initials to prevent accidental text selection
- `e.stopPropagation()` on star button prevents row navigation when tapping star
- All buttons have explicit `cursor: pointer`
- Form inputs use correct `type` attributes (tel, email, text)

---

## 8. Data Model

```js
Contact {
  id:       number      // auto-increment from 13
  first:    string      // required
  last:     string      // optional
  phone:    string      // required
  email:    string      // optional
  company:  string      // optional
  favorite: boolean     // default false
}
```

**Sorting:** Alphabetical by `first + ' ' + last` (localeCompare)

---

## 9. Technical Notes

- Built as a DC (Design Component) — single streaming `Contacts Manager.dc.html`
- State managed via `DCLogic` class (React class component pattern)
- Rendered inside `AndroidDevice` frame (412×892px, 8px border, status bar 40px, nav bar 24px → **812px usable content height**)
- Screens use `position: absolute; inset: 0` for zero-flicker switching
- Avatar colors: deterministic hash of `first + last` → one of 12 preset colors
- Search: real-time filtering on every `onInput` event
- Favorite animation: `favAnimId` state triggers keyframe, resets after 650ms
- Snackbar: auto-dismiss via `setTimeout` (3000ms), clears previous timer on new toast
- Font: Plus Jakarta Sans via Google Fonts CDN

---

*Contacts Manager · Android Design · June 2026*
