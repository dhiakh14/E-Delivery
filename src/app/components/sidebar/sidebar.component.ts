import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from '../../core/services/token-storage.service';

interface MenuItem {
  label: string;
  icon: string;      // nom de l'icône (voir ngSwitch dans le HTML)
  route: string;
  roles?: string[];
  badge?: number;
}

interface MenuSection {
  title: string;
  items: MenuItem[];
}

const DESKTOP_BREAKPOINT = 992;
const COLLAPSE_KEY = 'sidebar-collapsed';

/**
 * Le composant sert de layout complet : sidebar + topbar + <router-outlet>.
 * Il remplace l'ancien LayoutComponent.
 */
@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit, OnDestroy {
  sections: MenuSection[] = [];
  user: any;

  collapsed = false;    // desktop : menu réduit (icônes seulement)
  mobileOpen = false;   // mobile / tablette : tiroir ouvert

  constructor(
    private tokenService: TokenStorageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.tokenService.getUser();
    const role = this.tokenService.getRole();

    try {
      this.collapsed = localStorage.getItem(COLLAPSE_KEY) === '1';
    } catch { /* stockage indisponible */ }

    const allSections: MenuSection[] = [
      {
        title: 'Principal',
        items: [
          { label: 'Tableau de bord', icon: 'dashboard',  route: '/app/dashboard' },
          { label: 'Commandes',       icon: 'orders',     route: '/app/orders', badge: 5 },
          { label: 'Restaurants',     icon: 'restaurant', route: '/app/restaurants' }
        ]
      },
      {
        title: 'Catalogue',
        items: [
          { label: 'Produits',        icon: 'products',   route: '/app/products' },
          { label: 'Nourriture',      icon: 'food',       route: '/app/food' }
        ]
      },
      {
        title: 'Gestion',
        items: [
          { label: 'Clients',         icon: 'clients',    route: '/app/clients' },
          { label: 'Utilisateurs',    icon: 'users',      route: '/app/users', roles: ['EMPLOYER'] }
        ]
      },
      {
        title: 'Système',
        items: [
          { label: 'Paramètres',      icon: 'settings',   route: '/app/settings' }
        ]
      }
    ];

    // Filtrer par rôle
    this.sections = allSections
      .map(sec => ({
        ...sec,
        items: sec.items.filter(i => !i.roles || i.roles.includes(role || ''))
      }))
      .filter(sec => sec.items.length > 0);
  }

  ngOnDestroy(): void {
    this.lockScroll(false);
  }

  get firstName(): string {
    const name: string = this.user?.fullName || '';
    return name.split(' ')[0] || 'Chef';
  }

  /** Bouton du topbar : tiroir sur mobile, réduction sur desktop */
  toggleMenu(): void {
    if (window.innerWidth < DESKTOP_BREAKPOINT) {
      this.mobileOpen ? this.closeMobile() : this.openMobile();
    } else {
      this.collapsed = !this.collapsed;
      try {
        localStorage.setItem(COLLAPSE_KEY, this.collapsed ? '1' : '0');
      } catch { /* ignore */ }
    }
  }

  openMobile(): void {
    this.mobileOpen = true;
    this.lockScroll(true);
  }

  closeMobile(): void {
    this.mobileOpen = false;
    this.lockScroll(false);
  }

  onItemClick(): void {
    if (window.innerWidth < DESKTOP_BREAKPOINT) this.closeMobile();
  }

  logout(): void {
    const svc: any = this.tokenService;
    if (typeof svc.signOut === 'function') {
      svc.signOut();
    } else if (typeof svc.logout === 'function') {
      svc.logout();
    } else {
      localStorage.clear();
      sessionStorage.clear();
    }
    this.closeMobile();
    this.router.navigate(['/login']);
  }

  getInitials(name?: string): string {
    if (!name) return '?';
    return name.split(' ').filter(Boolean).map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  /** Ferme le tiroir si on repasse en desktop */
  @HostListener('window:resize')
  onResize(): void {
    if (window.innerWidth >= DESKTOP_BREAKPOINT && this.mobileOpen) this.closeMobile();
  }

  @HostListener('document:keydown.escape')
  onEscape(): void {
    if (this.mobileOpen) this.closeMobile();
  }

  private lockScroll(lock: boolean): void {
    document.body.style.overflow = lock ? 'hidden' : '';
  }
}