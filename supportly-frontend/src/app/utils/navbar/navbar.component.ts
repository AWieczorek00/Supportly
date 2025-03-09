import {Component} from '@angular/core';
import {MatToolbar} from '@angular/material/toolbar';
import {MatAnchor, MatIconButton} from '@angular/material/button';
import {Router, RouterLink} from '@angular/router';
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';

@Component({
  selector: 'app-navbar',
  imports: [
    MatToolbar,
    MatIconButton,
    MatMenuTrigger,
    MatMenu,
    MatAnchor,
    RouterLink,
    MatMenuItem
  ],
  templateUrl: './navbar.component.html',
  standalone: true,
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  // constructor(private _router: Router, public dialog: MatDialog) {
  // }

  constructor(private router: Router) {
  }

  // logout() {
  //   // localStorage.clear();
  //   // this._router.navigate(['/login']);
  // }

  open() {
    // this.dialog.open(SmallWindowsMenuComponent, {
    //   height: '100%',
    //   position: {right:'0px'}
    // });
  }

  logout() {
    console.log("User logged out");
  }

  toggleMenu() {
    console.log("Menu toggled");
    // Możesz użyć toggle lub zdefiniować więcej funkcji do obsługi
  }

}
