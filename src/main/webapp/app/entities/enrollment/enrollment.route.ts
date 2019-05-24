import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Enrollment } from 'app/shared/model/enrollment.model';
import { EnrollmentService } from './enrollment.service';
import { EnrollmentComponent } from './enrollment.component';
import { EnrollmentDetailComponent } from './enrollment-detail.component';
import { EnrollmentUpdateComponent } from './enrollment-update.component';
import { EnrollmentDeletePopupComponent } from './enrollment-delete-dialog.component';
import { IEnrollment } from 'app/shared/model/enrollment.model';

@Injectable({ providedIn: 'root' })
export class EnrollmentResolve implements Resolve<IEnrollment> {
  constructor(private service: EnrollmentService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEnrollment> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Enrollment>) => response.ok),
        map((enrollment: HttpResponse<Enrollment>) => enrollment.body)
      );
    }
    return of(new Enrollment());
  }
}

export const enrollmentRoute: Routes = [
  {
    path: '',
    component: EnrollmentComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Enrollments'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EnrollmentDetailComponent,
    resolve: {
      enrollment: EnrollmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Enrollments'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EnrollmentUpdateComponent,
    resolve: {
      enrollment: EnrollmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Enrollments'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EnrollmentUpdateComponent,
    resolve: {
      enrollment: EnrollmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Enrollments'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const enrollmentPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: EnrollmentDeletePopupComponent,
    resolve: {
      enrollment: EnrollmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Enrollments'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
