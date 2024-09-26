import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { switchMap, take, tap } from 'rxjs/operators';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> {
    return this.authService.autoLogin().pipe(
      switchMap((autoLoginSuccess) => {
        if (autoLoginSuccess) {
          return this.authService.isLoggedIn$.pipe(
            take(1),
            switchMap((isLoggedIn) => {
              if (isLoggedIn) {
                return this.authService.role$.pipe(
                  take(1),
                  switchMap((userRole) => {
                    const requiredRoles = (next.data as { roles: string[] }).roles;
                    if (requiredRoles.includes(userRole)) {
                      return of(true);
                    } else {
                      this.router.navigate(['/unauthorized']);
                      return of(false);
                    }
                  })
                );
              } else {
                this.router.navigate(['/login']);
                return of(false);
              }
            })
          );
        } else {
          this.router.navigate(['/login']);
          return of(false);
        }
      })
    );
  }
}
