import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from '../../core/services/token-storage.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  user: any;

  constructor(private tokenService: TokenStorageService, private router: Router) {}

  ngOnInit(): void {
    this.user = this.tokenService.getUser();
  }

  logout(): void {
    this.tokenService.clear();
    this.router.navigate(['/login']);
  }
}