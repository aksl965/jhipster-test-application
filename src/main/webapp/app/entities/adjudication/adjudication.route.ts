import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Adjudication } from 'app/shared/model/adjudication.model';
import { AdjudicationService } from './adjudication.service';
import { AdjudicationComponent } from './adjudication.component';
import { AdjudicationDetailComponent } from './adjudication-detail.component';
import { AdjudicationUpdateComponent } from './adjudication-update.component';
import { AdjudicationDeletePopupComponent } from './adjudication-delete-dialog.component';
import { IAdjudication } from 'app/shared/model/adjudication.model';

@Injectable({ providedIn: 'root' })
export class AdjudicationResolve implements Resolve<IAdjudication> {
  constructor(private service: AdjudicationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAdjudication> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Adjudication>) => response.ok),
        map((adjudication: HttpResponse<Adjudication>) => adjudication.body)
      );
    }
    return of(new Adjudication());
  }
}

export const adjudicationRoute: Routes = [
  {
    path: '',
    component: AdjudicationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Adjudications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AdjudicationDetailComponent,
    resolve: {
      adjudication: AdjudicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Adjudications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AdjudicationUpdateComponent,
    resolve: {
      adjudication: AdjudicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Adjudications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AdjudicationUpdateComponent,
    resolve: {
      adjudication: AdjudicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Adjudications'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const adjudicationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: AdjudicationDeletePopupComponent,
    resolve: {
      adjudication: AdjudicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Adjudications'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
