import { Moment } from 'moment';
import { IPatient } from 'app/shared/model/patient.model';

export interface IEnrollment {
  id?: number;
  planType?: string;
  plantName?: string;
  premiumValue?: number;
  fromDate?: Moment;
  toDate?: Moment;
  content?: any;
  patient?: IPatient;
}

export class Enrollment implements IEnrollment {
  constructor(
    public id?: number,
    public planType?: string,
    public plantName?: string,
    public premiumValue?: number,
    public fromDate?: Moment,
    public toDate?: Moment,
    public content?: any,
    public patient?: IPatient
  ) {}
}
